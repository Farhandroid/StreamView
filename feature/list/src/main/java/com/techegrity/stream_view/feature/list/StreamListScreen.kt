package com.techegrity.stream_view.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techegrity.stream_view.core.ui.components.AppTopBar
import com.techegrity.stream_view.core.ui.components.EmptyView
import com.techegrity.stream_view.core.ui.components.ErrorView
import com.techegrity.stream_view.core.ui.components.LoadingView
import com.techegrity.stream_view.core.ui.components.StreamCard
import com.techegrity.stream_view.core.ui.components.StreamFab
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme
import com.techegrity.stream_view.domain.model.Stream
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StreamListRoute(
    onNavigateToPlayer: (String) -> Unit,
    viewModel: StreamViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is StreamListEffect.NavigateToPlayer -> onNavigateToPlayer(effect.streamId)
            }
        }
    }

    StreamListScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun StreamListScreen(
    state: StreamListState,
    onIntent: (StreamListIntent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.stream_list_title),
                searchExpanded = state.searchExpanded,
                searchQuery = state.query,
                onSearchQueryChange = { onIntent(StreamListIntent.QueryChanged(it)) },
                onSearchToggle = { onIntent(StreamListIntent.ToggleSearch) },
                onSearchClear = { onIntent(StreamListIntent.ClearSearch) },
            )
        },
        floatingActionButton = {
            StreamFab(onClick = { onIntent(StreamListIntent.OpenAddDialog) })
        },
    ) { innerPadding ->
        when {
            state.isLoading && state.streams.isEmpty() -> {
                LoadingView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }
            state.errorMessage != null && state.streams.isEmpty() -> {
                ErrorView(
                    message = state.errorMessage,
                    onRetry = { onIntent(StreamListIntent.Retry) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }
            state.filtered.isEmpty() -> {
                EmptyView(
                    title = stringResource(R.string.stream_list_empty_title),
                    message = if (state.query.isBlank()) {
                        stringResource(R.string.stream_list_empty_no_data)
                    } else {
                        stringResource(R.string.stream_list_empty_no_results)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }
            else -> {
                StreamListContent(
                    streams = state.filtered,
                    totalCount = state.filtered.size,
                    onStreamClick = { onIntent(StreamListIntent.StreamTapped(it)) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }
        }
    }

    if (state.isAddDialogVisible) {
        AddStreamDialog(
            onDismiss = { onIntent(StreamListIntent.DismissAddDialog) },
            onConfirm = { name, url ->
                onIntent(StreamListIntent.AddStream(name = name, streamUrl = url))
            },
        )
    }
}

@Composable
private fun StreamListContent(
    streams: List<Stream>,
    totalCount: Int,
    onStreamClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = StreamViewSpacing.MarginMobile,
            end = StreamViewSpacing.MarginMobile,
            top = StreamViewSpacing.Md,
            bottom = StreamViewSpacing.Xl,
        ),
        verticalArrangement = Arrangement.spacedBy(StreamViewSpacing.Lg),
    ) {
        item {
            Column {
                Text(
                    text = stringResource(R.string.stream_list_header),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.stream_list_subtitle, totalCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        items(streams, key = { it.id }) { stream ->
            StreamCard(
                name = stream.name,
                streamUrl = stream.streamUrl,
                isConnected = stream.isConnected,
                metadata = stringResource(
                    R.string.stream_metadata_format,
                    stream.provider,
                    stream.metadata,
                ),
                onClick = { onStreamClick(stream.id) },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131313, heightDp = 900)
@Composable
private fun StreamListScreenPreview() {
    StreamViewTheme(darkTheme = true) {
        StreamListScreen(
            state = StreamListState(
                streams = listOf(
                    Stream(
                        id = "mux-bbb",
                        name = "Mux Big Buck Bunny",
                        thumbnailUrl = "",
                        streamUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
                        isConnected = true,
                        provider = "Mux",
                        metadata = "VOD",
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}
