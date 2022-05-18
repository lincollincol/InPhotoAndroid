package com.linc.inphoto.ui.view.imageoverlay

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.net.URL
import kotlin.coroutines.CoroutineContext

class BitmapLoadingWorkerJob internal constructor(
    private val overlayImageViewReference: WeakReference<MotionOverlayImageView>,
    val uri: Uri
) : CoroutineScope {

    private var currentJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + currentJob

    fun start() {
        currentJob = launch(Dispatchers.Default) {
            try {
                if (isActive) {
                    val url = URL(uri.toString())
                    val bitmap = BitmapFactory.decodeStream(url.openStream())
                    onPostExecute(Result(bitmap = bitmap))
//                    bitmap.recycle()
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
                    it.onStickerLoadingAsyncComplete(result)
                }
            }
            if (!completeCalled && result.bitmap != null) {
                // fast release of unused bitmap
                result.bitmap.recycle()
            }
        }
    }

    fun cancel() {
        currentJob.cancel()
    }

    /** The result of BitmapLoadingWorkerJob async loading.  */
    companion object
    class Result {
        val bitmap: Bitmap?
        val error: Exception?

        constructor(bitmap: Bitmap?) {
            this.bitmap = bitmap
            this.error = null
        }

        constructor(error: Exception?) {
            this.bitmap = null
            this.error = error
        }
    }
}