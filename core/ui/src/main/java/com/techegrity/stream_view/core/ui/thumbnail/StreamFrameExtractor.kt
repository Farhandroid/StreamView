package com.techegrity.stream_view.core.ui.thumbnail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.Surface
import com.techegrity.stream_view.core.ui.theme.UiConstants
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.resume
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

sealed interface FrameResult {
    data class Ready(val bitmap: Bitmap) : FrameResult
    data object Unavailable : FrameResult
}

object StreamFrameExtractor {

    private const val WIDTH = 640
    private const val HEIGHT = 360
    private const val TIMEOUT_MS = 12_000L
    private const val MAX_VIDEO_BITRATE = 1_200_000
    private const val POST_FRAME_DELAY_MS = 150L
    private const val RETRY_DELAY_MS = 300L
    private const val MAX_CAPTURE_ATTEMPTS = 2
    private const val MAX_PARALLEL_CAPTURES = 2
    private const val BLACK_LUMA_THRESHOLD = 18
    private const val BLACK_RATIO_THRESHOLD = 0.92f
    private const val MAX_CACHE_SIZE = 32

    private val results = ConcurrentHashMap<String, FrameResult>()
    private val inFlight = ConcurrentHashMap<String, CompletableDeferred<FrameResult>>()
    private val attempted = ConcurrentHashMap.newKeySet<String>()
    private val captureSlots = Semaphore(MAX_PARALLEL_CAPTURES)
    private val mainHandler = Handler(Looper.getMainLooper())

    fun peek(url: String): FrameResult? {
        if (url.isBlank()) return FrameResult.Unavailable
        return results[url]
    }

    fun hasAttempted(url: String): Boolean =
        url.isBlank() || attempted.contains(url) || results.containsKey(url)

    fun markAttempted(url: String) {
        if (url.isNotBlank()) attempted.add(url)
    }

    suspend fun getFrame(context: Context, streamUrl: String): FrameResult {
        if (streamUrl.isBlank()) return FrameResult.Unavailable
        markAttempted(streamUrl)
        results[streamUrl]?.let { return it }

        while (true) {
            results[streamUrl]?.let { return it }

            val existingJob = inFlight[streamUrl]
            if (existingJob != null) {
                return try {
                    existingJob.await()
                } catch (_: kotlinx.coroutines.CancellationException) {
                    results[streamUrl] ?: continue
                }
            }

            val deferred = CompletableDeferred<FrameResult>()
            val winner = inFlight.putIfAbsent(streamUrl, deferred)
            if (winner != null) {
                return try {
                    winner.await()
                } catch (_: kotlinx.coroutines.CancellationException) {
                    results[streamUrl] ?: continue
                }
            }

            return try {
                val frame = captureSlots.withPermit {
                    withTimeoutOrNull(TIMEOUT_MS) {
                        captureWithExoPlayer(context.applicationContext, streamUrl)
                    }
                }
                val result = if (frame != null && !frame.isMostlyBlack()) {
                    trimCacheIfNeeded()
                    FrameResult.Ready(frame)
                } else {
                    frame?.recycle()
                    FrameResult.Unavailable
                }
                results[streamUrl] = result
                deferred.complete(result)
                result
            } catch (cancelled: kotlinx.coroutines.CancellationException) {
                if (!deferred.isCompleted) {
                    deferred.cancel()
                }
                throw cancelled
            } catch (_: Throwable) {
                val result = FrameResult.Unavailable
                results[streamUrl] = result
                deferred.complete(result)
                result
            } finally {
                inFlight.remove(streamUrl, deferred)
            }
        }
    }

    private fun trimCacheIfNeeded() {
        if (results.size <= MAX_CACHE_SIZE) return
        val readyKeys = results.entries
            .filter { it.value is FrameResult.Ready }
            .map { it.key }
        readyKeys.take((readyKeys.size - MAX_CACHE_SIZE / 2).coerceAtLeast(0)).forEach { key ->
            val removed = results.remove(key)
            (removed as? FrameResult.Ready)?.bitmap?.recycle()
        }
    }

    private suspend fun captureWithExoPlayer(
        context: Context,
        streamUrl: String,
    ): Bitmap? = withContext(Dispatchers.Main) {
        val firstFrame = CompletableDeferred<Unit>()
        val playbackError = CompletableDeferred<Unit>()

        val trackSelector = DefaultTrackSelector(context).apply {
            parameters = buildUponParameters()
                .setMaxVideoBitrate(MAX_VIDEO_BITRATE)
                .setForceHighestSupportedBitrate(false)
                .build()
        }
        val surfaceTexture = SurfaceTexture(/* texName = */ 0).apply {
            setDefaultBufferSize(WIDTH, HEIGHT)
        }
        val surface = Surface(surfaceTexture)
        val player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()

        fun cleanup() {
            runCatching { player.release() }
            runCatching { surface.release() }
            runCatching { surfaceTexture.release() }
        }

        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                if (!firstFrame.isCompleted) {
                    firstFrame.complete(Unit)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                if (!playbackError.isCompleted) {
                    playbackError.complete(Unit)
                }
            }
        }

        try {
            player.addListener(listener)
            player.setVideoSurface(surface)
            player.setMediaItem(MediaItem.fromUri(streamUrl))
            player.volume = UiConstants.VOLUME_MUTED
            player.playWhenReady = true
            player.prepare()

            val ready = withTimeoutOrNull(TIMEOUT_MS) {
                select {
                    firstFrame.onAwait { true }
                    playbackError.onAwait { false }
                }
            }
            if (ready != true) {
                return@withContext null
            }

            repeat(MAX_CAPTURE_ATTEMPTS) { attempt ->
                delay(if (attempt == 0) POST_FRAME_DELAY_MS else RETRY_DELAY_MS)
                val bitmap = copyFrame(surface) ?: return@repeat
                if (!bitmap.isMostlyBlack()) {
                    return@withContext bitmap
                }
                bitmap.recycle()
            }
            null
        } catch (cancelled: kotlinx.coroutines.CancellationException) {
            throw cancelled
        } catch (_: Throwable) {
            null
        } finally {
            runCatching { player.removeListener(listener) }
            cleanup()
        }
    }

    private suspend fun copyFrame(surface: Surface): Bitmap? =
        suspendCancellableCoroutine { continuation ->
            val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
            PixelCopy.request(surface, bitmap, { copyResult ->
                if (!continuation.isActive) {
                    bitmap.recycle()
                    return@request
                }
                if (copyResult == PixelCopy.SUCCESS) {
                    continuation.resume(bitmap)
                } else {
                    bitmap.recycle()
                    continuation.resume(null)
                }
            }, mainHandler)
        }

    private fun Bitmap.isMostlyBlack(): Boolean {
        var dark = 0
        var total = 0
        val stepX = (width / 32).coerceAtLeast(1)
        val stepY = (height / 32).coerceAtLeast(1)
        var y = 0
        while (y < height) {
            var x = 0
            while (x < width) {
                val color = getPixel(x, y)
                val r = (color shr 16) and 0xFF
                val g = (color shr 8) and 0xFF
                val b = color and 0xFF
                val luma = (r * 299 + g * 587 + b * 114) / 1000
                if (luma < BLACK_LUMA_THRESHOLD) dark++
                total++
                x += stepX
            }
            y += stepY
        }
        return total == 0 || dark.toFloat() / total >= BLACK_RATIO_THRESHOLD
    }
}
