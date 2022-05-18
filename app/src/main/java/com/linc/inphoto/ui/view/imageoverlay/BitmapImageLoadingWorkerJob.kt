package com.linc.inphoto.ui.view.imageoverlay

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.linc.inphoto.utils.extensions.getFileBytes
import com.linc.inphoto.utils.extensions.isUrl
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.net.URL
import kotlin.coroutines.CoroutineContext

class BitmapImageLoadingWorkerJob constructor(
    private val context: Context,
    private val overlayImageViewReference: WeakReference<MotionOverlayImageView>,
    private val uri: Uri,
    private val isLayerResource: Boolean
) : CoroutineScope {

    private var currentJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + currentJob

    fun start() {
        currentJob = launch(Dispatchers.Default) {
            try {
                if (isActive) {
                    val bitmap = when {
                        uri.isUrl() -> BitmapFactory.decodeStream(URL(uri.toString()).openStream())
                        else -> uri.getFileBytes(context)?.let {
                            BitmapFactory.decodeByteArray(it, 0, it.count())
                        }
                    }
                    onPostExecute(Result(bitmap = bitmap))
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
                    when {
                        isLayerResource -> it.onLayerLoadingAsyncComplete(result)
                        else -> it.onImageLoadingAsyncComplete(result)
                    }
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