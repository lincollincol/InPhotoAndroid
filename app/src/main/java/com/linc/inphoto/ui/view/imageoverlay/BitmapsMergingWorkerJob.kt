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

class BitmapsMergingWorkerJob(
    private val context: Context,
    private val overlayImageViewReference: WeakReference<MotionOverlayImageView>,
    private val motionViewReference: WeakReference<MotionView>
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun start() {
        job = launch(Dispatchers.Default) {
            try {
                if (isActive) {
                    val editableImageBitmap = overlayImageViewReference.get()?.getBitmap()
                    val stickersBitmap = motionViewReference.get()?.thumbnailImage
                    if (editableImageBitmap == null || stickersBitmap == null) {
                        throw KotlinNullPointerException("Image or stickers not found!")
                    }
                    val outputBitmap = Bitmap.createBitmap(
                        editableImageBitmap.width,
                        editableImageBitmap.height,
                        editableImageBitmap.config
                    )
                    val canvas = Canvas(outputBitmap)
                    canvas.drawBitmap(editableImageBitmap, 0F, 0F, null)
                    canvas.drawBitmap(stickersBitmap, 0F, 0F, null)
                    onPostExecute(Result(context.createTempUri(outputBitmap)))
                    outputBitmap.recycle()
                    editableImageBitmap.recycle()
                    stickersBitmap.recycle()
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
                    it.onImageMergingAsyncComplete(result)
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