package com.techegrity.stream_view.feature.list

import com.techegrity.stream_view.domain.model.Stream

data class StreamListState(
    val isLoading: Boolean = false,
    val streams: List<Stream> = emptyList(),
    val query: String = "",
    val searchExpanded: Boolean = false,
    val errorMessage: String? = null,
    val isAddDialogVisible: Boolean = false,
    val addErrorMessage: String? = null,
) {
    val filtered: List<Stream>
        get() {
            val withUrl = streams.filter { it.streamUrl.isNotBlank() }
            return if (query.isBlank()) {
                withUrl
            } else {
                withUrl.filter { stream ->
                    stream.name.contains(query, ignoreCase = true) ||
                        stream.streamUrl.contains(query, ignoreCase = true) ||
                        stream.provider.contains(query, ignoreCase = true)
                }
            }
        }
}

sealed interface StreamListIntent {
    data object Load : StreamListIntent
    data object Retry : StreamListIntent
    data class QueryChanged(val query: String) : StreamListIntent
    data object ToggleSearch : StreamListIntent
    data object ClearSearch : StreamListIntent
    data class StreamTapped(val id: String) : StreamListIntent
    data object OpenAddDialog : StreamListIntent
    data object DismissAddDialog : StreamListIntent
    data class AddStream(val name: String, val streamUrl: String) : StreamListIntent
}

sealed interface StreamListEffect {
    data class NavigateToPlayer(val streamId: String) : StreamListEffect
}
