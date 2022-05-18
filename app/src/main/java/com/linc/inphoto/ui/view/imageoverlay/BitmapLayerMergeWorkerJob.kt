package com.linc.inphoto.ui.view.imageoverlay

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.net.Uri
import com.linc.inphoto.utils.extensions.createTempUri
import com.linc.inphoto.utils.extensions.view.crop
import com.linc.inphoto.utils.extensions.view.resize
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class BitmapLayerMergeWorkerJob(
    private val context: Context,
    private val overlayImageViewReference: WeakReference<MotionOverlayImageView>,
    private val imagePoint: Point,
    private val imageBitmap: Bitmap,
    private val layersBitmap: Bitmap
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun start() {
        job = launch(Dispatchers.Default) {
            try {
                if (isActive) {
                    val preparedLayersBitmap = layersBitmap.crop(
                        0,
                        imagePoint.y,
                        layersBitmap.width,
                        imageBitmap.height
                    )?.resize(imageBitmap.width, imageBitmap.height)
                    val outputBitmap = Bitmap.createBitmap(
                        imageBitmap.width,
                        imageBitmap.height,
                        imageBitmap.config
                    )
                    val canvas = Canvas(outputBitmap)
                    canvas.drawBitmap(imageBitmap, 0F, 0F, null)
                    canvas.drawBitmap(preparedLayersBitmap!!, 0F, 0F, null)
                    val outputImageUri = context.createTempUri(outputBitmap)
                    outputBitmap.recycle()
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