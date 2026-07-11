package com.techegrity.stream_view.feature.player

import android.content.Context
import java.util.concurrent.TimeUnit

internal object PlayerTimeFormatter {
    fun format(context: Context, positionMs: Long): String {
        if (positionMs < PlayerConstants.POSITION_START_MS) {
            return context.getString(R.string.player_time_zero)
        }
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(positionMs)
        val hours = totalSeconds / PlayerConstants.SECONDS_PER_HOUR
        val minutes =
            (totalSeconds % PlayerConstants.SECONDS_PER_HOUR) / PlayerConstants.SECONDS_PER_MINUTE
        val seconds = totalSeconds % PlayerConstants.SECONDS_PER_MINUTE
        return if (hours > PlayerConstants.ZERO_LONG) {
            context.getString(
                R.string.player_time_hms,
                hours.toInt(),
                minutes.toInt(),
                seconds.toInt(),
            )
        } else {
            context.getString(
                R.string.player_time_ms,
                minutes.toInt(),
                seconds.toInt(),
            )
        }
    }
}
