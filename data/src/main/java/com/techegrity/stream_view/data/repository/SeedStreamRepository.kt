package com.techegrity.stream_view.data.repository

import com.techegrity.stream_view.data.source.PublicStreamCatalog
import com.techegrity.stream_view.data.source.StreamCatalogConstants
import com.techegrity.stream_view.domain.model.Stream
import com.techegrity.stream_view.domain.repository.StreamRepository
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * In-memory [StreamRepository] seeded from [PublicStreamCatalog], plus session-only user adds.
 * Not a test double — this is the demo app's real data source until a remote backend exists.
 */
class SeedStreamRepository : StreamRepository {

    private val mutex = Mutex()
    private val _streams = MutableStateFlow(
        PublicStreamCatalog.streams.filter { it.streamUrl.isNotBlank() },
    )

    override fun observeStreams(): Flow<List<Stream>> = _streams.asStateFlow()

    override suspend fun getStreams(): List<Stream> {
        delay(StreamCatalogConstants.NETWORK_DELAY_MS)
        return _streams.value
    }

    override suspend fun getStreamById(id: String): Stream? {
        delay(StreamCatalogConstants.LOOKUP_DELAY_MS)
        return _streams.value.firstOrNull { it.id == id }
    }

    override suspend fun addStream(name: String, streamUrl: String): Stream {
        require(name.isNotBlank()) { StreamCatalogConstants.VALIDATION_NAME_REQUIRED }
        require(streamUrl.isNotBlank()) { StreamCatalogConstants.VALIDATION_URL_REQUIRED }

        val stream = Stream(
            id = "${StreamCatalogConstants.USER_STREAM_ID_PREFIX}${UUID.randomUUID()}",
            name = name,
            thumbnailUrl = "",
            streamUrl = streamUrl,
            provider = StreamCatalogConstants.PROVIDER_USER,
            metadata = StreamCatalogConstants.META_USER_ADDED,
        )

        mutex.withLock {
            _streams.update { current -> listOf(stream) + current }
        }
        return stream
    }
}
