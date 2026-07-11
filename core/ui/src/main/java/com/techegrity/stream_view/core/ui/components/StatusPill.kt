package com.techegrity.stream_view.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.StatusConnected
import com.techegrity.stream_view.core.ui.theme.StatusDisconnected
import com.techegrity.stream_view.core.ui.theme.StreamViewRadii
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme

@Composable
fun StatusPill(
    connected: Boolean,
    modifier: Modifier = Modifier,
) {
    val indicatorColor = if (connected) StatusConnected else StatusDisconnected
    val label = if (connected) "Connected" else "Disconnected"

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(StreamViewRadii.Pill))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.92f))
            .padding(horizontal = StreamViewSpacing.Sm, vertical = StreamViewSpacing.Xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StreamViewSpacing.Xs),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(indicatorColor),
        )
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun StatusPillConnectedPreview() {
    StreamViewTheme(darkTheme = true) {
        StatusPill(connected = true)
    }
}

@Preview
@Composable
private fun StatusPillDisconnectedPreview() {
    StreamViewTheme(darkTheme = true) {
        StatusPill(connected = false)
    }
}
