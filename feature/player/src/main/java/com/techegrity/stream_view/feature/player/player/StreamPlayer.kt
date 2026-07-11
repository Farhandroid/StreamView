package com.techegrity.stream_view.feature.player.player

import androidx.media3.common.Player
import kotlinx.coroutines.flow.Flow

sealed interface PlayerEvent {
    data object Idle : PlayerEvent
    data object Loading : PlayerEvent
    data object Ready : PlayerEvent
    data object Buffering : PlayerEvent
    data object Ended : PlayerEvent
    data class Error(val reason: String) : PlayerEvent
    data class IsPlayingChanged(val isPlaying: Boolean) : PlayerEvent
    data class Progress(
        val positionMs: Long,
        val bufferedMs: Long,
        val durationMs: Long,
    ) : PlayerEvent
}

interface StreamPlayer {
    val player: Player
    val events: Flow<PlayerEvent>

    fun prepare(url: String)
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun setVolume(volume: Float)
    fun release()
}
