package com.techegrity.stream_view.feature.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.Primary
import com.techegrity.stream_view.core.ui.theme.UiConstants
import com.techegrity.stream_view.feature.player.PlayerConstants

@Composable
internal fun PlayerProgressBar(
    progress: Float,
    buffered: Float,
    onSeekFraction: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(UiConstants.PLAYER_PROGRESS_TOUCH_HEIGHT_DP.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(UiConstants.PLAYER_PROGRESS_HEIGHT_DP.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.outlineVariant.copy(
                        alpha = UiConstants.PLAYER_PROGRESS_INACTIVE_ALPHA,
                    ),
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        buffered.coerceIn(
                            PlayerConstants.PROGRESS_MIN,
                            PlayerConstants.PROGRESS_MAX,
                        ),
                    )
                    .height(UiConstants.PLAYER_PROGRESS_HEIGHT_DP.dp)
                    .background(Color.White.copy(alpha = UiConstants.PLAYER_BUFFERED_TRACK_ALPHA)),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        progress.coerceIn(
                            PlayerConstants.PROGRESS_MIN,
                            PlayerConstants.PROGRESS_MAX,
                        ),
                    )
                    .height(UiConstants.PLAYER_PROGRESS_HEIGHT_DP.dp)
                    .background(Primary),
            )
        }
        Slider(
            value = progress.coerceIn(PlayerConstants.PROGRESS_MIN, PlayerConstants.PROGRESS_MAX),
            onValueChange = onSeekFraction,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Primary,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
            ),
        )
    }
}
