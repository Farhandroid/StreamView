package com.techegrity.stream_view.core.ui.thumbnail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.Surface
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.resume
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
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
    private const val BLACK_LUMA_THRESHOLD = 18
    private const val BLACK_RATIO_THRESHOLD = 0.92f
    private const val MAX_CACHE_SIZE = 32

    private val results = ConcurrentHashMap<String, FrameResult>()
    private val inFlight = ConcurrentHashMap<String, CompletableDeferred<FrameResult>>()
    private val attempted = ConcurrentHashMap.newKeySet<String>()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun peek(url: String): FrameResult? {
        if (url.isBlank()) return FrameResult.Unavailable
        return results[url]
    }

    /** True after the first load for this URL has been started (survives list recycle). */
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
                    // Extractor was cancelled by a recycled item; retry below.
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
                val frame = withTimeoutOrNull(TIMEOUT_MS) {
                    captureWithExoPlayer(context.applicationContext, streamUrl)
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
                // Scroll recycle cancelled this job — do not cache failure so a visible item can retry.
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
        suspendCancellableCoroutine { continuation ->
            val surfaceTexture = SurfaceTexture(/* texName = */ 0).apply {
                setDefaultBufferSize(WIDTH, HEIGHT)
            }
            val surface = Surface(surfaceTexture)
            val player = ExoPlayer.Builder(context).build()

            fun cleanup() {
                runCatching { player.release() }
                runCatching { surface.release() }
                runCatching { surfaceTexture.release() }
            }

            continuation.invokeOnCancellation { cleanup() }

            val listener = object : Player.Listener {
                override fun onRenderedFirstFrame() {
                    player.removeListener(this)
                    val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
                    PixelCopy.request(surface, bitmap, { copyResult ->
                        cleanup()
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

                override fun onPlayerError(error: PlaybackException) {
                    player.removeListener(this)
                    cleanup()
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
            }

            player.addListener(listener)
            player.setVideoSurface(surface)
            player.setMediaItem(MediaItem.fromUri(streamUrl))
            player.volume = 0f
            player.playWhenReady = true
            player.prepare()
        }
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
