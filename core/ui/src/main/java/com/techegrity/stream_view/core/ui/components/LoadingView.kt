package com.techegrity.stream_view.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techegrity.stream_view.core.ui.theme.StreamViewRadii
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    centered: Boolean = false,
) {
    if (centered) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = StreamViewSpacing.MarginMobile),
            verticalArrangement = Arrangement.spacedBy(StreamViewSpacing.Md),
        ) {
            repeat(3) {
                ShimmerStreamCardPlaceholder()
            }
        }
    }
}

@Composable
fun ShimmerStreamCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerX",
    )

    val base = MaterialTheme.colorScheme.surfaceContainerHigh
    val highlight = MaterialTheme.colorScheme.surfaceContainerHighest
    val brush = Brush.linearGradient(
        colors = listOf(base, highlight, base),
        start = Offset(shimmerX - 200f, 0f),
        end = Offset(shimmerX, 200f),
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(StreamViewRadii.Card))
                .background(brush),
        )
        Spacer(modifier = Modifier.height(StreamViewSpacing.Sm))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .height(16.dp)
                .clip(RoundedCornerShape(StreamViewRadii.Sm))
                .background(brush),
        )
        Spacer(modifier = Modifier.height(StreamViewSpacing.Xs))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .height(12.dp)
                .clip(RoundedCornerShape(StreamViewRadii.Sm))
                .background(brush),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131313)
@Composable
private fun LoadingViewPreview() {
    StreamViewTheme(darkTheme = true) {
        LoadingView()
    }
}
