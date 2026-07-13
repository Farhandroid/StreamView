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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.techegrity.stream_view.core.ui.theme.UiConstants
import com.techegrity.stream_view.core.ui.thumbnail.FrameResult
import com.techegrity.stream_view.core.ui.thumbnail.StreamFrameExtractor

/**
 * Prefers a live frame from [streamUrl]. Falls back to [fallbackImageUrl] (no error UI).
 * Spinner only on the first load of a URL.
 */
@Composable
fun StreamThumbnail(
    streamUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallbackImageUrl: String = "",
) {
    val context = LocalContext.current
    val initial = remember(streamUrl) { StreamFrameExtractor.peek(streamUrl) }
    var bitmap by remember(streamUrl) {
        mutableStateOf((initial as? FrameResult.Ready)?.bitmap)
    }
    var useFallback by remember(streamUrl, fallbackImageUrl) {
        mutableStateOf(
            initial is FrameResult.Unavailable && fallbackImageUrl.isNotBlank(),
        )
    }
    var showSpinner by remember(streamUrl) {
        val firstTime = streamUrl.isNotBlank() &&
            initial == null &&
            !StreamFrameExtractor.hasAttempted(streamUrl)
        if (firstTime) {
            StreamFrameExtractor.markAttempted(streamUrl)
        }
        mutableStateOf(firstTime)
    }

    LaunchedEffect(streamUrl, fallbackImageUrl) {
        if (streamUrl.isBlank()) {
            bitmap = null
            useFallback = fallbackImageUrl.isNotBlank()
            showSpinner = false
            return@LaunchedEffect
        }
        when (val peeked = StreamFrameExtractor.peek(streamUrl)) {
            is FrameResult.Ready -> {
                bitmap = peeked.bitmap
                useFallback = false
                showSpinner = false
                return@LaunchedEffect
            }
            FrameResult.Unavailable -> {
                bitmap = null
                useFallback = fallbackImageUrl.isNotBlank()
                showSpinner = false
                return@LaunchedEffect
            }
            null -> Unit
        }
        StreamFrameExtractor.markAttempted(streamUrl)
        when (val result = StreamFrameExtractor.getFrame(context, streamUrl)) {
            is FrameResult.Ready -> {
                bitmap = result.bitmap
                useFallback = false
            }
            FrameResult.Unavailable -> {
                bitmap = null
                useFallback = fallbackImageUrl.isNotBlank()
            }
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
            useFallback -> {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(fallbackImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            showSpinner -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = UiConstants.THUMBNAIL_SPINNER_STROKE_WIDTH_DP.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Outlined.PlayCircle,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(UiConstants.SIGNAL_LOST_ICON_SIZE_DP.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
