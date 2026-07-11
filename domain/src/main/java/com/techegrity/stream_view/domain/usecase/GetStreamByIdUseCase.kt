package com.techegrity.stream_view.domain.usecase

import com.techegrity.stream_view.domain.model.Stream
import com.techegrity.stream_view.domain.repository.StreamRepository

class GetStreamByIdUseCase(
    private val repository: StreamRepository,
) {
    suspend operator fun invoke(id: String): Stream? = repository.getStreamById(id)
}
