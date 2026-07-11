package com.techegrity.stream_view.feature.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.PlayerOverlay
import com.techegrity.stream_view.core.ui.theme.UiConstants
import com.techegrity.stream_view.feature.player.R

@Composable
internal fun PlayerCenterPlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(UiConstants.PLAYER_CENTER_BUTTON_SIZE_DP.dp)
            .clip(CircleShape)
            .background(PlayerOverlay)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
            contentDescription = stringResource(
                if (isPlaying) R.string.player_pause else R.string.player_play,
            ),
            tint = Color.White,
            modifier = Modifier.size(UiConstants.PLAYER_CENTER_ICON_SIZE_DP.dp),
        )
    }
}
