package com.techegrity.stream_view.feature.player

import com.techegrity.stream_view.domain.model.Stream

data class StreamPlayerState(
    val stream: Stream? = null,
    val playback: Playback = Playback.Idle,
    val isPlaying: Boolean = false,
    val positionMs: Long = PlayerConstants.POSITION_START_MS,
    val bufferedMs: Long = PlayerConstants.POSITION_START_MS,
    val durationMs: Long = PlayerConstants.UNKNOWN_DURATION_MS,
    val volume: Float = PlayerConstants.VOLUME_DEFAULT,
    val controlsVisible: Boolean = true,
    val errorMessage: String? = null,
    val hasNext: Boolean = false,
) {
    val isLive: Boolean
        get() = durationMs <= PlayerConstants.MIN_VALID_DURATION_MS

    val progressFraction: Float
        get() = if (durationMs > PlayerConstants.MIN_VALID_DURATION_MS) {
            (positionMs.toFloat() / durationMs.toFloat()).coerceIn(
                PlayerConstants.PROGRESS_MIN,
                PlayerConstants.PROGRESS_MAX,
            )
        } else {
            PlayerConstants.PROGRESS_MIN
        }

    val bufferedFraction: Float
        get() = if (durationMs > PlayerConstants.MIN_VALID_DURATION_MS) {
            (bufferedMs.toFloat() / durationMs.toFloat()).coerceIn(
                PlayerConstants.PROGRESS_MIN,
                PlayerConstants.PROGRESS_MAX,
            )
        } else {
            PlayerConstants.PROGRESS_MIN
        }

    sealed interface Playback {
        data object Idle : Playback
        data object Loading : Playback
        data object Ready : Playback
        data object Buffering : Playback
        data object Ended : Playback
        data class Error(val reason: String) : Playback
    }
}

sealed interface StreamPlayerIntent {
    data object Retry : StreamPlayerIntent
    data object TogglePlayPause : StreamPlayerIntent
    data object PlayNext : StreamPlayerIntent
    data object ShowControls : StreamPlayerIntent
    data object HideControls : StreamPlayerIntent
    data object ToggleControls : StreamPlayerIntent
    data class Seek(val positionMs: Long) : StreamPlayerIntent
    data class SetVolume(val volume: Float) : StreamPlayerIntent
    data object Release : StreamPlayerIntent
}

sealed interface StreamPlayerEffect {
    data object NavigateBack : StreamPlayerEffect
}
