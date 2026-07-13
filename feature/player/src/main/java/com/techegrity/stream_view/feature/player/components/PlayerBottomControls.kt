package com.techegrity.stream_view.feature.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material.icons.outlined.FullscreenExit
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.Primary
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing
import com.techegrity.stream_view.core.ui.theme.UiConstants
import com.techegrity.stream_view.feature.player.PlayerConstants
import com.techegrity.stream_view.feature.player.PlayerTimeFormatter
import com.techegrity.stream_view.feature.player.R
import com.techegrity.stream_view.feature.player.StreamPlayerIntent
import com.techegrity.stream_view.feature.player.StreamPlayerState

@Composable
internal fun PlayerBottomControls(
    state: StreamPlayerState,
    isFullscreen: Boolean,
    onIntent: (StreamPlayerIntent) -> Unit,
    onToggleFullscreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var volumeExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = UiConstants.PLAYER_BOTTOM_GRADIENT_MID_ALPHA),
                        Color.Black.copy(alpha = UiConstants.PLAYER_BOTTOM_GRADIENT_END_ALPHA),
                    ),
                ),
            )
            .navigationBarsPadding()
            .padding(
                start = StreamViewSpacing.MarginMobile,
                end = StreamViewSpacing.MarginMobile,
                top = StreamViewSpacing.Xl,
                bottom = StreamViewSpacing.Lg,
            ),
    ) {
        if (!state.isLive) {
            PlayerProgressBar(
                progress = state.progressFraction,
                buffered = state.bufferedFraction,
                onSeekFraction = { fraction ->
                    val duration = state.durationMs
                    if (duration > PlayerConstants.MIN_VALID_DURATION_MS) {
                        onIntent(StreamPlayerIntent.Seek((duration * fraction).toLong()))
                    }
                },
            )
            Spacer(modifier = Modifier.height(StreamViewSpacing.Md))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(StreamViewSpacing.Md),
            ) {
                IconButton(onClick = { onIntent(StreamPlayerIntent.TogglePlayPause) }) {
                    Icon(
                        imageVector = if (state.isPlaying) {
                            Icons.Outlined.Pause
                        } else {
                            Icons.Outlined.PlayArrow
                        },
                        contentDescription = stringResource(
                            if (state.isPlaying) R.string.player_pause else R.string.player_play,
                        ),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(UiConstants.PLAYER_PLAY_ICON_SIZE_DP.dp),
                    )
                }
                IconButton(
                    onClick = { onIntent(StreamPlayerIntent.PlayNext) },
                    enabled = state.hasNext,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SkipNext,
                        contentDescription = stringResource(R.string.player_skip_next),
                        tint = if (state.hasNext) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = UiConstants.PLAYER_DISABLED_ICON_ALPHA,
                            )
                        },
                        modifier = Modifier.size(UiConstants.PLAYER_CONTROL_ICON_SIZE_DP.dp),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { volumeExpanded = !volumeExpanded }) {
                        Icon(
                            imageVector = if (state.volume <= PlayerConstants.VOLUME_MUTED_THRESHOLD) {
                                Icons.AutoMirrored.Outlined.VolumeOff
                            } else {
                                Icons.AutoMirrored.Outlined.VolumeUp
                            },
                            contentDescription = stringResource(R.string.player_volume),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                    AnimatedVisibility(visible = volumeExpanded) {
                        Slider(
                            value = state.volume,
                            onValueChange = { onIntent(StreamPlayerIntent.SetVolume(it)) },
                            modifier = Modifier.width(UiConstants.PLAYER_VOLUME_SLIDER_WIDTH_DP.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = Primary,
                                activeTrackColor = Primary,
                                inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(
                                    alpha = UiConstants.PLAYER_SLIDER_INACTIVE_ALPHA,
                                ),
                            ),
                        )
                    }
                }
                if (state.isLive) {
                    Text(
                        text = stringResource(R.string.player_live),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.player_time_range,
                            PlayerTimeFormatter.format(context, state.positionMs),
                            stringResource(R.string.player_time_separator),
                            PlayerTimeFormatter.format(
                                context,
                                state.durationMs.coerceAtLeast(PlayerConstants.POSITION_START_MS),
                            ),
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(StreamViewSpacing.Sm),
            ) {
                IconButton(onClick = onToggleFullscreen) {
                    Icon(
                        imageVector = if (isFullscreen) {
                            Icons.Outlined.FullscreenExit
                        } else {
                            Icons.Outlined.Fullscreen
                        },
                        contentDescription = stringResource(R.string.player_fullscreen),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(UiConstants.PLAYER_CONTROL_ICON_SIZE_DP.dp),
                    )
                }
            }
        }
    }
}
