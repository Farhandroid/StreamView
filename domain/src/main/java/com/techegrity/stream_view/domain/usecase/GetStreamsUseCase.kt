package com.techegrity.stream_view.domain.usecase

import com.techegrity.stream_view.domain.model.Stream
import com.techegrity.stream_view.domain.repository.StreamRepository

class GetStreamsUseCase(
    private val repository: StreamRepository,
) {
    suspend operator fun invoke(): List<Stream> = repository.getStreams()
}
