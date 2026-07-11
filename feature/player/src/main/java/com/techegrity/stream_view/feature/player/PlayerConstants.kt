package com.techegrity.stream_view.feature.player

/**
 * Player-domain numeric / key constants. User-visible copy lives in `strings.xml`.
 */
object PlayerConstants {
    const val UNKNOWN_DURATION_MS = -1L
    const val POSITION_START_MS = 0L
    const val ZERO_LONG = 0L
    const val MIN_VALID_DURATION_MS = 0L
    const val VOLUME_MIN = 0f
    const val VOLUME_MAX = 1f
    const val VOLUME_DEFAULT = 1f
    const val VOLUME_MUTED = 0f
    const val VOLUME_MUTED_THRESHOLD = 0.001f
    const val PROGRESS_MIN = 0f
    const val PROGRESS_MAX = 1f
    const val SECONDS_PER_HOUR = 3600L
    const val SECONDS_PER_MINUTE = 60L
    const val PROGRESS_POLL_MS = 500L

    const val ERROR_NOT_FOUND = "not_found"
    const val ARG_STREAM_ID = "streamId"
}
