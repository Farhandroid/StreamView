package com.techegrity.stream_view.domain.usecase

import com.techegrity.stream_view.domain.model.Stream
import com.techegrity.stream_view.domain.repository.StreamRepository

class AddStreamUseCase(
    private val repository: StreamRepository,
) {
    suspend operator fun invoke(name: String, streamUrl: String): Stream =
        repository.addStream(name = name.trim(), streamUrl = streamUrl.trim())
}
