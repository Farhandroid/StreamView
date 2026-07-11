package com.techegrity.stream_view.ui.list

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.techegrity.stream_view.core.ui.components.AppTopBar
import com.techegrity.stream_view.core.ui.components.EmptyView
import com.techegrity.stream_view.core.ui.components.StreamCard
import com.techegrity.stream_view.core.ui.components.StreamFab
import com.techegrity.stream_view.core.ui.theme.StreamViewSpacing
import com.techegrity.stream_view.core.ui.theme.StreamViewTheme

data class StreamListItem(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val metadata: String,
    val isConnected: Boolean,
)

/** Design mock data matching the Stitch Active Streams screen. */
val stitchStreamListItems = listOf(
    StreamListItem(
        id = "cam-01",
        name = "Front Entrance",
        thumbnailUrl = "https://picsum.photos/seed/frontentrance/800/450",
        metadata = "1080p • 60fps • Cam 01",
        isConnected = true,
    ),
    StreamListItem(
        id = "cam-04",
        name = "Main Lobby",
        thumbnailUrl = "https://picsum.photos/seed/mainlobby/800/450",
        metadata = "4K • 30fps • Cam 04",
        isConnected = true,
    ),
    StreamListItem(
        id = "cam-12",
        name = "Parking Level B1",
        thumbnailUrl = "https://picsum.photos/seed/parkingb1/800/450",
        metadata = "1080p • 24fps • Cam 12",
        isConnected = true,
    ),
    StreamListItem(
        id = "cam-08",
        name = "Server Room",
        thumbnailUrl = "https://picsum.photos/seed/serverroom/800/450",
        metadata = "720p • 60fps • Cam 08",
        isConnected = true,
    ),
    StreamListItem(
        id = "cam-02",
        name = "North Courtyard",
        thumbnailUrl = "https://picsum.photos/seed/northcourtyard/800/450",
        metadata = "1080p • 30fps • Cam 02",
        isConnected = true,
    ),
    StreamListItem(
        id = "cam-09",
        name = "West Gate",
        thumbnailUrl = "",
        metadata = "Retry in 12s • Cam 09",
        isConnected = false,
    ),
)

@Composable
fun StreamListScreen(
    streams: List<StreamListItem> = stitchStreamListItems,
    onStreamClick: (String) -> Unit = {},
    onMoreClick: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
) {
    var searchExpanded by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val filtered = remember(streams, searchQuery) {
        if (searchQuery.isBlank()) streams
        else streams.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "StreamView",
                searchExpanded = searchExpanded,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearchToggle = {
                    searchExpanded = !searchExpanded
                    if (!searchExpanded) searchQuery = ""
                },
                onSearchClear = { searchQuery = "" },
            )
        },
        floatingActionButton = {
            StreamFab(onClick = onAddClick)
        },
    ) { innerPadding ->
        StreamListContent(
            streams = filtered,
            totalCount = streams.size,
            searchQuery = searchQuery,
            onStreamClick = onStreamClick,
            onMoreClick = onMoreClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@Composable
private fun StreamListContent(
    streams: List<StreamListItem>,
    totalCount: Int,
    searchQuery: String,
    onStreamClick: (String) -> Unit,
    onMoreClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (streams.isEmpty()) {
        EmptyView(
            title = "No streams found",
            message = if (searchQuery.isBlank()) {
                "No active streams to monitor."
            } else {
                "Try a different search term."
            },
            modifier = modifier,
        )
        return
    }

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
                    text = "Active Streams",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Monitoring $totalCount locations in real-time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        items(streams, key = { it.id }) { stream ->
            StreamCard(
                name = stream.name,
                thumbnailUrl = stream.thumbnailUrl,
                isConnected = stream.isConnected,
                metadata = stream.metadata,
                onClick = { onStreamClick(stream.id) },
                onMoreClick = { onMoreClick(stream.id) },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131313, heightDp = 900)
@Composable
private fun StreamListScreenPreview() {
    StreamViewTheme(darkTheme = true) {
        StreamListScreen()
    }
}
