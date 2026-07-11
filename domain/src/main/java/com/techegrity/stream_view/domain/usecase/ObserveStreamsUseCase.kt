package com.techegrity.stream_view.domain.usecase

import com.techegrity.stream_view.domain.model.Stream
import com.techegrity.stream_view.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow

class ObserveStreamsUseCase(
    private val repository: StreamRepository,
) {
    operator fun invoke(): Flow<List<Stream>> = repository.observeStreams()
}
