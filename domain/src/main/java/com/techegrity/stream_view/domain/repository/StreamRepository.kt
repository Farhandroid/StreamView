package com.techegrity.stream_view.domain.repository

import com.techegrity.stream_view.domain.model.Stream
import kotlinx.coroutines.flow.Flow

interface StreamRepository {
    fun observeStreams(): Flow<List<Stream>>
    suspend fun getStreams(): List<Stream>
    suspend fun getStreamById(id: String): Stream?
    suspend fun addStream(name: String, streamUrl: String): Stream
}
