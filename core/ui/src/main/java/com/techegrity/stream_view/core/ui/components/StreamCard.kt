package com.techegrity.stream_view.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.R
import com.techegrity.stream_view.core.ui.theme.StreamViewRadii
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme
import com.techegrity.stream_view.core.ui.theme.UiConstants

/**
 * @param isConnected Whether the **feed** is available (not thumbnail load state).
 */
@Composable
fun StreamCard(
    name: String,
    streamUrl: String,
    isConnected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    metadata: String? = null,
    onMoreClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(UiConstants.STREAM_THUMBNAIL_ASPECT_RATIO)
                .clip(RoundedCornerShape(StreamViewRadii.Thumbnail))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            if (isConnected) {
                StreamThumbnail(
                    streamUrl = streamUrl,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.VideocamOff,
                        contentDescription = null,
                        modifier = Modifier.size(UiConstants.SIGNAL_LOST_ICON_SIZE_DP.dp),
                        tint = MaterialTheme.colorScheme.outline,
                    )
                    Spacer(modifier = Modifier.height(StreamViewSpacing.Sm))
                    Text(
                        text = stringResource(R.string.signal_lost),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
            StatusPill(
                connected = isConnected,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(StreamViewSpacing.Sm),
            )
        }
        Spacer(modifier = Modifier.height(StreamViewSpacing.Sm))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!metadata.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = metadata,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            if (onMoreClick != null) {
                IconButton(
                    onClick = onMoreClick,
                    modifier = Modifier.size(StreamViewSpacing.MinTouchTarget),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(R.string.cd_more_options),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131313)
@Composable
private fun StreamCardPreview() {
    StreamViewTheme(darkTheme = true) {
        StreamCard(
            name = "Mux Big Buck Bunny",
            streamUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
            isConnected = true,
            metadata = "Mux • VOD • ABR",
            onClick = {},
            modifier = Modifier.padding(StreamViewSpacing.Md),
        )
    }
}
