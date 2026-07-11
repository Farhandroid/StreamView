package com.techegrity.stream_view.feature.player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.techegrity.stream_view.core.ui.components.ErrorView
import com.techegrity.stream_view.core.ui.theme.Primary
import com.techegrity.stream_view.core.ui.theme.UiConstants
import com.techegrity.stream_view.feature.player.components.PlayerBottomControls
import com.techegrity.stream_view.feature.player.components.PlayerCenterPlayButton
import com.techegrity.stream_view.feature.player.components.PlayerTopBar
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun StreamPlayerScreen(
    state: StreamPlayerState,
    player: Player,
    onIntent: (StreamPlayerIntent) -> Unit,
    onBack: () -> Unit,
    onPlayerViewCreated: (PlayerView) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isFullscreen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity
    val view = LocalView.current

    DisposableEffect(lifecycleOwner, player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> player.pause()
                Lifecycle.Event.ON_START -> {
                    if (player.playWhenReady) {
                        player.play()
                    }
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(state.controlsVisible, state.isPlaying) {
        if (state.controlsVisible && state.isPlaying) {
            delay(UiConstants.PLAYER_CONTROLS_HIDE_MS.milliseconds)
            onIntent(StreamPlayerIntent.HideControls)
        }
    }

    DisposableEffect(isFullscreen) {
        val window = activity?.window
        val controller = window?.let {
            WindowCompat.getInsetsController(it, view)
        }
        if (isFullscreen) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            controller?.hide(WindowInsetsCompat.Type.systemBars())
            controller?.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            controller?.show(WindowInsetsCompat.Type.systemBars())
        }
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            controller?.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onIntent(StreamPlayerIntent.ToggleControls) },
                )
            },
    ) {
        AndroidView(
            factory = { ctx ->
                val parent = FrameLayout(ctx)
                (
                    LayoutInflater.from(ctx)
                        .inflate(R.layout.view_stream_player, parent, false) as PlayerView
                    ).also { playerView ->
                    playerView.player = player
                    onPlayerViewCreated(playerView)
                }
            },
            update = { playerView ->
                if (playerView.player !== player) {
                    playerView.player = player
                }
            },
            onRelease = { playerView ->
                playerView.player = null
                playerView.visibility = View.GONE
            },
            modifier = Modifier.fillMaxSize(),
        )

        val showBuffering = state.playback is StreamPlayerState.Playback.Loading ||
            state.playback is StreamPlayerState.Playback.Buffering
        if (showBuffering && state.errorMessage == null) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(UiConstants.PLAYER_LOADING_SPINNER_SIZE_DP.dp)
                    .align(Alignment.Center),
                color = Primary,
                strokeWidth = UiConstants.PLAYER_SPINNER_STROKE_WIDTH_DP.dp,
            )
        }

        when (state.playback) {
            is StreamPlayerState.Playback.Error -> {
                val message = if (state.errorMessage == PlayerConstants.ERROR_NOT_FOUND) {
                    stringResource(R.string.player_stream_not_found)
                } else {
                    stringResource(R.string.player_playback_error)
                }
                ErrorView(
                    message = message,
                    onRetry = { onIntent(StreamPlayerIntent.Retry) },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = UiConstants.PLAYER_ERROR_SCRIM_ALPHA)),
                )
            }
            else -> Unit
        }

        AnimatedVisibility(
            visible = state.controlsVisible && state.playback !is StreamPlayerState.Playback.Error,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                PlayerTopBar(
                    title = state.stream?.name.orEmpty(),
                    subtitle = state.stream?.let {
                        stringResource(
                            R.string.player_metadata_format,
                            it.provider,
                            it.metadata,
                        )
                    }.orEmpty(),
                    onBack = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                )

                PlayerCenterPlayButton(
                    isPlaying = state.isPlaying,
                    onClick = { onIntent(StreamPlayerIntent.TogglePlayPause) },
                    modifier = Modifier.align(Alignment.Center),
                )

                PlayerBottomControls(
                    state = state,
                    isFullscreen = isFullscreen,
                    onIntent = onIntent,
                    onToggleFullscreen = { isFullscreen = !isFullscreen },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                )
            }
        }
    }
}
