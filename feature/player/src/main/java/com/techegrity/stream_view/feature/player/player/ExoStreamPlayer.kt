package com.techegrity.stream_view.feature.player.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.techegrity.stream_view.feature.player.PlayerConstants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ExoStreamPlayer(
    context: Context,
) : StreamPlayer {

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context.applicationContext).build()

    override val player: Player
        get() = exoPlayer

    override val events: Flow<PlayerEvent> = callbackFlow {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> trySend(PlayerEvent.Idle)
                    Player.STATE_BUFFERING -> trySend(PlayerEvent.Buffering)
                    Player.STATE_READY -> trySend(PlayerEvent.Ready)
                    Player.STATE_ENDED -> trySend(PlayerEvent.Ended)
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                trySend(PlayerEvent.IsPlayingChanged(isPlaying))
            }

            override fun onPlayerError(error: PlaybackException) {
                trySend(
                    PlayerEvent.Error(
                        error.message ?: error.errorCodeName,
                    ),
                )
            }
        }

        exoPlayer.addListener(listener)

        val progressJob = launch {
            while (isActive) {
                val duration = exoPlayer.duration
                trySend(
                    PlayerEvent.Progress(
                        positionMs = exoPlayer.currentPosition.coerceAtLeast(
                            PlayerConstants.POSITION_START_MS,
                        ),
                        bufferedMs = exoPlayer.bufferedPosition.coerceAtLeast(
                            PlayerConstants.POSITION_START_MS,
                        ),
                        durationMs = if (duration > PlayerConstants.MIN_VALID_DURATION_MS) {
                            duration
                        } else {
                            PlayerConstants.UNKNOWN_DURATION_MS
                        },
                    ),
                )
                delay(PlayerConstants.PROGRESS_POLL_MS)
            }
        }

        awaitClose {
            progressJob.cancel()
            exoPlayer.removeListener(listener)
        }
    }.distinctUntilChanged()

    override fun prepare(url: String) {
        exoPlayer.setMediaItem(MediaItem.fromUri(url))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun play() {
        exoPlayer.playWhenReady = true
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs.coerceAtLeast(PlayerConstants.POSITION_START_MS))
    }

    override fun setVolume(volume: Float) {
        exoPlayer.volume = volume.coerceIn(
            PlayerConstants.VOLUME_MIN,
            PlayerConstants.VOLUME_MAX,
        )
    }

    override fun release() {
        exoPlayer.release()
    }
}
