package com.techegrity.stream_view.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.UiConstants
import com.techegrity.stream_view.core.ui.thumbnail.FrameResult
import com.techegrity.stream_view.core.ui.thumbnail.StreamFrameExtractor

/**
 * Spinner only on the first load of a URL. Scroll recycle uses play icon / cached frame.
 */
@Composable
fun StreamThumbnail(
    streamUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val initial = remember(streamUrl) { StreamFrameExtractor.peek(streamUrl) }
    var bitmap by remember(streamUrl) {
        mutableStateOf((initial as? FrameResult.Ready)?.bitmap)
    }
    // Spinner only on the very first load of this URL (not when LazyColumn recycles).
    var showSpinner by remember(streamUrl) {
        val firstTime = streamUrl.isNotBlank() &&
            initial == null &&
            !StreamFrameExtractor.hasAttempted(streamUrl)
        if (firstTime) {
            StreamFrameExtractor.markAttempted(streamUrl)
        }
        mutableStateOf(firstTime)
    }

    LaunchedEffect(streamUrl) {
        if (streamUrl.isBlank()) {
            bitmap = null
            showSpinner = false
            return@LaunchedEffect
        }
        when (val peeked = StreamFrameExtractor.peek(streamUrl)) {
            is FrameResult.Ready -> {
                bitmap = peeked.bitmap
                showSpinner = false
                return@LaunchedEffect
            }
            FrameResult.Unavailable -> {
                bitmap = null
                showSpinner = false
                return@LaunchedEffect
            }
            null -> Unit
        }
        StreamFrameExtractor.markAttempted(streamUrl)
        when (val result = StreamFrameExtractor.getFrame(context, streamUrl)) {
            is FrameResult.Ready -> bitmap = result.bitmap
            FrameResult.Unavailable -> bitmap = null
        }
        showSpinner = false
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center,
    ) {
        when {
            bitmap != null -> {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            showSpinner -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Outlined.PlayCircle,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(UiConstants.SIGNAL_LOST_ICON_SIZE_DP.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
