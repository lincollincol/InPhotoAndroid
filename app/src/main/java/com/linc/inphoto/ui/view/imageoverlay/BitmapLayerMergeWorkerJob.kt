package com.linc.inphoto.ui.view.imageoverlay

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import com.linc.inphoto.utils.extensions.createTempUri
import com.linc.inphoto.utils.extensions.view.getBitmap
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


class BitmapLayerMergeWorkerJob(
    private val context: Context,
    private val motionViewReference: WeakReference<MotionView>,
    private val overlayImageViewReference: WeakReference<MotionOverlayImageView>,
    private val originalImageBitmap: Bitmap
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun start() {
        job = launch(Dispatchers.Default) {
            try {
                if (isActive) {
                    val imageViewRect =
                        overlayImageViewReference.get()?.getBitmapRect() ?: return@launch
                    val imageBitmap = overlayImageViewReference.get()?.getBitmap() ?: return@launch
                    val layersBitmap = motionViewReference.get()?.getBitmap(
                        desiredWidth = imageBitmap.width
                    ) ?: kotlin.run {
                        imageBitmap.recycle()
                        val outputImageUri = context.createTempUri(originalImageBitmap)
                        onPostExecute(Result(outputImageUri))
                        return@launch
                    }
                    val mergedBitmap = Bitmap.createBitmap(
                        imageBitmap.width,
                        imageBitmap.height,
                        imageBitmap.config
                    )
                    val canvas = Canvas(mergedBitmap)
                    canvas.drawBitmap(imageBitmap, 0F, 0F, null)
                    canvas.drawBitmap(layersBitmap, 0F, 0F, null)
                    val resultBitmap = Bitmap.createBitmap(
                        mergedBitmap,
                        0,
                        imageViewRect.top,
                        imageBitmap.width,
                        imageViewRect.height()
                    )
                    val outputImageUri = context.createTempUri(resultBitmap)
                    imageBitmap.recycle()
                    layersBitmap.recycle()
                    mergedBitmap.recycle()
                    resultBitmap.recycle()
                    originalImageBitmap.recycle()
                    onPostExecute(Result(outputImageUri))
                }
            } catch (e: Exception) {
                onPostExecute(Result(e))
            }
        }
    }

    private suspend fun onPostExecute(result: Result) {
        withContext(Dispatchers.Main) {
            var completeCalled = false
            if (isActive) {
                overlayImageViewReference.get()?.let {
                    completeCalled = true
                    it.onImageLayersMergeAsyncComplete(result)
                }
            }
        }
    }

    fun cancel() {
        job.cancel()
    }

    class Result {
        val uri: Uri?
        val error: Exception?

        constructor(uri: Uri?) {
            this.uri = uri
            this.error = null
        }

        constructor(error: Exception?) {
            this.error = error
            this.uri = null
        }
    }
}