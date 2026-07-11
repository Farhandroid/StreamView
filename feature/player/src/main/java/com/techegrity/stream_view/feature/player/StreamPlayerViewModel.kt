package com.techegrity.stream_view.feature.player

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.techegrity.stream_view.domain.model.Stream
import com.techegrity.stream_view.domain.usecase.ObserveStreamsUseCase
import com.techegrity.stream_view.feature.player.player.ExoStreamPlayer
import com.techegrity.stream_view.feature.player.player.PlayerEvent
import com.techegrity.stream_view.feature.player.player.StreamPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StreamPlayerViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val observeStreams: ObserveStreamsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var currentStreamId: String = checkNotNull(savedStateHandle[ARG_STREAM_ID]) {
        context.getString(R.string.player_missing_stream_id)
    }

    private val streamPlayer: StreamPlayer = ExoStreamPlayer(context)
    private var playableStreams: List<Stream> = emptyList()
    private var hasStartedInitialPlayback: Boolean = false

    val player: Player
        get() = streamPlayer.player

    private val _state = MutableStateFlow(StreamPlayerState(playback = StreamPlayerState.Playback.Loading))
    val state: StateFlow<StreamPlayerState> = _state.asStateFlow()

    private val _effects = Channel<StreamPlayerEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        observePlayerEvents()
        observePlaylist()
    }

    fun onIntent(intent: StreamPlayerIntent) {
        when (intent) {
            StreamPlayerIntent.Retry -> retry()
            StreamPlayerIntent.TogglePlayPause -> togglePlayPause()
            StreamPlayerIntent.PlayNext -> playNext()
            StreamPlayerIntent.ShowControls -> _state.update { it.copy(controlsVisible = true) }
            StreamPlayerIntent.HideControls -> _state.update { it.copy(controlsVisible = false) }
            StreamPlayerIntent.ToggleControls -> {
                _state.update { it.copy(controlsVisible = !it.controlsVisible) }
            }
            is StreamPlayerIntent.Seek -> streamPlayer.seekTo(intent.positionMs)
            is StreamPlayerIntent.SetVolume -> {
                streamPlayer.setVolume(intent.volume)
                _state.update { it.copy(volume = intent.volume) }
            }
            StreamPlayerIntent.Release -> streamPlayer.release()
        }
    }

    private fun observePlaylist() {
        viewModelScope.launch {
            observeStreams().collect { streams ->
                playableStreams = streams.filter { it.streamUrl.isNotBlank() }
                val current = playableStreams.firstOrNull { it.id == currentStreamId }
                _state.update {
                    it.copy(
                        stream = current ?: it.stream,
                        hasNext = playableStreams.size > 1,
                    )
                }
                if (!hasStartedInitialPlayback) {
                    hasStartedInitialPlayback = true
                    if (current != null) {
                        prepareStream(current)
                    } else {
                        emitNotFound()
                    }
                }
            }
        }
    }

    private fun prepareStream(stream: Stream) {
        currentStreamId = stream.id
        _state.update {
            it.copy(
                stream = stream,
                hasNext = playableStreams.size > 1,
                playback = StreamPlayerState.Playback.Loading,
                errorMessage = null,
                positionMs = PlayerConstants.POSITION_START_MS,
                bufferedMs = PlayerConstants.POSITION_START_MS,
                durationMs = PlayerConstants.UNKNOWN_DURATION_MS,
                controlsVisible = true,
            )
        }
        streamPlayer.prepare(stream.streamUrl)
    }

    private fun retry() {
        val stream = _state.value.stream
            ?: playableStreams.firstOrNull { it.id == currentStreamId }
        if (stream == null || stream.streamUrl.isBlank()) {
            emitNotFound()
            return
        }
        prepareStream(stream)
    }

    private fun emitNotFound() {
        _state.update {
            it.copy(
                playback = StreamPlayerState.Playback.Error(PlayerConstants.ERROR_NOT_FOUND),
                errorMessage = PlayerConstants.ERROR_NOT_FOUND,
            )
        }
    }

    private fun togglePlayPause() {
        if (_state.value.isPlaying) {
            streamPlayer.pause()
        } else {
            streamPlayer.play()
        }
    }

    private fun playNext() {
        val next = nextPlayableStream() ?: return
        prepareStream(next)
    }

    private fun nextPlayableStream(): Stream? {
        val streams = playableStreams
        if (streams.size <= 1) return null
        val index = streams.indexOfFirst { it.id == currentStreamId }
        if (index < 0) return streams.firstOrNull()
        return streams[(index + 1) % streams.size]
    }

    private fun observePlayerEvents() {
        viewModelScope.launch {
            streamPlayer.events.collect { event ->
                when (event) {
                    PlayerEvent.Idle -> {
                        // Ignore pre-prepare idle; otherwise first open never starts playback.
                        if (!hasStartedInitialPlayback) return@collect
                        _state.update { it.copy(playback = StreamPlayerState.Playback.Idle) }
                    }
                    PlayerEvent.Loading -> {
                        _state.update { it.copy(playback = StreamPlayerState.Playback.Loading) }
                    }
                    PlayerEvent.Ready -> {
                        _state.update {
                            it.copy(
                                playback = StreamPlayerState.Playback.Ready,
                                errorMessage = null,
                            )
                        }
                    }
                    PlayerEvent.Buffering -> {
                        _state.update { it.copy(playback = StreamPlayerState.Playback.Buffering) }
                    }
                    PlayerEvent.Ended -> {
                        _state.update {
                            it.copy(
                                playback = StreamPlayerState.Playback.Ended,
                                isPlaying = false,
                            )
                        }
                        if (_state.value.hasNext) {
                            playNext()
                        }
                    }
                    is PlayerEvent.Error -> {
                        _state.update {
                            it.copy(
                                playback = StreamPlayerState.Playback.Error(event.reason),
                                errorMessage = event.reason,
                                isPlaying = false,
                            )
                        }
                    }
                    is PlayerEvent.IsPlayingChanged -> {
                        _state.update { it.copy(isPlaying = event.isPlaying) }
                    }
                    is PlayerEvent.Progress -> {
                        _state.update {
                            it.copy(
                                positionMs = event.positionMs,
                                bufferedMs = event.bufferedMs,
                                durationMs = event.durationMs,
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        streamPlayer.release()
        super.onCleared()
    }

    companion object {
        const val ARG_STREAM_ID = PlayerConstants.ARG_STREAM_ID
    }
}
