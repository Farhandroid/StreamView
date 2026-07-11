package com.techegrity.stream_view.feature.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techegrity.stream_view.core.common.dispatchers.DispatcherProvider
import com.techegrity.stream_view.domain.usecase.AddStreamUseCase
import com.techegrity.stream_view.domain.usecase.ObserveStreamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val observeStreams: ObserveStreamsUseCase,
    private val addStream: AddStreamUseCase,
    private val dispatchers: DispatcherProvider,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(StreamListState(isLoading = true))
    val state: StateFlow<StreamListState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<StreamListEffect>()
    val effects: SharedFlow<StreamListEffect> = _effects.asSharedFlow()

    init {
        observeCatalog()
    }

    fun onIntent(intent: StreamListIntent) {
        when (intent) {
            StreamListIntent.Load, StreamListIntent.Retry -> {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
            }
            is StreamListIntent.QueryChanged -> _state.update { it.copy(query = intent.query) }
            StreamListIntent.ToggleSearch -> _state.update { current ->
                val expanded = !current.searchExpanded
                current.copy(
                    searchExpanded = expanded,
                    query = if (expanded) current.query else "",
                )
            }
            StreamListIntent.ClearSearch -> _state.update { it.copy(query = "") }
            is StreamListIntent.StreamTapped -> viewModelScope.launch {
                _effects.emit(StreamListEffect.NavigateToPlayer(intent.id))
            }
            StreamListIntent.OpenAddDialog -> _state.update {
                it.copy(isAddDialogVisible = true, addErrorMessage = null)
            }
            StreamListIntent.DismissAddDialog -> _state.update {
                it.copy(isAddDialogVisible = false, addErrorMessage = null)
            }
            is StreamListIntent.AddStream -> addUserStream(intent.name, intent.streamUrl)
        }
    }

    private fun observeCatalog() {
        viewModelScope.launch(dispatchers.io) {
            observeStreams()
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                                ?: context.getString(R.string.stream_list_load_error),
                        )
                    }
                }
                .collect { streams ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            streams = streams,
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    private fun addUserStream(name: String, streamUrl: String) {
        viewModelScope.launch(dispatchers.io) {
            runCatching { addStream(name, streamUrl) }
                .onSuccess {
                    _state.update {
                        it.copy(isAddDialogVisible = false, addErrorMessage = null)
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            addErrorMessage = error.message
                                ?: context.getString(R.string.add_stream_failed),
                        )
                    }
                }
        }
    }
}
