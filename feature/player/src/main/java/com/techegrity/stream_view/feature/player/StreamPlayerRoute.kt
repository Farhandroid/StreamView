package com.techegrity.stream_view.feature.player

import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StreamPlayerRoute(
    onNavigateBack: () -> Unit,
    viewModel: StreamPlayerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var playerView by remember { mutableStateOf<PlayerView?>(null) }
    val latestOnNavigateBack by rememberUpdatedState(onNavigateBack)
    val latestViewModel by rememberUpdatedState(viewModel)

    val navigateBackCleanly = remember {
        {
            playerView?.let { view ->
                view.player = null
                view.visibility = View.GONE
            }
            latestViewModel.player.pause()
            latestOnNavigateBack()
        }
    }

    LaunchedEffect(latestViewModel) {
        latestViewModel.effects.collectLatest { effect ->
            when (effect) {
                StreamPlayerEffect.NavigateBack -> navigateBackCleanly()
            }
        }
    }

    BackHandler(onBack = navigateBackCleanly)

    StreamPlayerScreen(
        state = state,
        player = latestViewModel.player,
        onIntent = latestViewModel::onIntent,
        onBack = navigateBackCleanly,
        onPlayerViewCreated = { playerView = it },
    )
}
