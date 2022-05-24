package com.linc.inphoto.ui.view.imageoverlay

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.linc.inphoto.databinding.LayoutMotionOverlayImageViewBinding
import com.linc.inphoto.ui.view.imageoverlay.model.ImageEntity
import com.linc.inphoto.ui.view.imageoverlay.model.Layer
import com.linc.inphoto.ui.view.imageoverlay.model.MotionEntity
import com.linc.inphoto.utils.extensions.inflater
import com.linc.inphoto.utils.extensions.view.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Runnable
import java.lang.ref.WeakReference


class MotionOverlayImageView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), MotionView.MotionViewCallback {

    private var binding: LayoutMotionOverlayImageViewBinding? = null
    private var onSaveImageListener: ((Uri?) -> Unit)? = null

    private var bitmapImageLoadingWorkerJob: WeakReference<BitmapImageLoadingWorkerJob>? = null
    private var bitmapLayerMergeWorkerJob: WeakReference<BitmapLayerMergeWorkerJob>? = null

    private var bitmap: Bitmap? = null

    override fun onEntitySelected(entity: MotionEntity?) {
        // Not implemented
    }

    override fun onEntityDoubleTap(entity: MotionEntity) {
        // Not implemented
    }

    override fun onEntityDeleteTap(entity: MotionEntity) {
        binding?.motionView?.deleteEntity(entity)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }
        binding = LayoutMotionOverlayImageViewBinding.inflate(context.inflater, this, true)
        binding?.run {
            motionView.setMotionViewCallback(this@MotionOverlayImageView)

        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
        onSaveImageListener = null
    }

    fun getBitmapRect() = binding?.imageView?.getBitmapCoordinatesInsideImageView()

    fun setOnSaveImageListener(onSaveImageListener: (Uri?) -> Unit) {
        this.onSaveImageListener = onSaveImageListener
    }

    fun onImageLayersMergeAsyncComplete(result: BitmapLayerMergeWorkerJob.Result) {
        binding?.progressBar?.hide()
        bitmapLayerMergeWorkerJob = null
        if (result.error != null) {
            Timber.e(result.error)
            return
        }
        binding?.motionView?.release()
        bitmap?.recycle()
        onSaveImageListener?.invoke(result.uri)
    }

    fun onLayerLoadingAsyncComplete(result: BitmapImageLoadingWorkerJob.Result) {
        bitmapImageLoadingWorkerJob = null
        if (result.error != null) {
            Timber.e(result.error)
            return
        }
        result.bitmap?.let(::addSticker)
    }

    fun onImageLoadingAsyncComplete(result: BitmapImageLoadingWorkerJob.Result) {
        binding?.progressBar?.hide()
        bitmapImageLoadingWorkerJob = null
        if (result.error != null) {
            Timber.e(result.error)
            return
        }
        binding?.imageView?.setImageBitmap(result.bitmap)
        bitmap = result.bitmap
    }

    fun saveImageAsync() {
        val originalBitmap = bitmap ?: return
        binding?.motionView?.unselectEntity()
        binding?.progressBar?.show()
        bitmapLayerMergeWorkerJob?.get()?.cancel()
        bitmapLayerMergeWorkerJob = WeakReference(
            BitmapLayerMergeWorkerJob(
                context = context,
                motionViewReference = WeakReference(binding?.motionView),
                overlayImageViewReference = WeakReference(this),
                originalBitmap
            )
        )
        bitmapLayerMergeWorkerJob?.get()?.start()
    }

    fun setImageUri(image: Uri?) {
        image ?: return
        binding?.progressBar?.show()
        bitmapImageLoadingWorkerJob?.get()
        bitmapImageLoadingWorkerJob = WeakReference(
            BitmapImageLoadingWorkerJob(
                context = context,
                overlayImageViewReference = WeakReference(this),
                uri = image,
                isLayerResource = false
            )
        )
        bitmapImageLoadingWorkerJob?.get()?.start()
    }

    fun addStickerAsync(image: Uri) {
        if (bitmapImageLoadingWorkerJob?.get() != null) {
            return
        }
        bitmapImageLoadingWorkerJob = WeakReference(
            BitmapImageLoadingWorkerJob(
                context = context,
                overlayImageViewReference = WeakReference(this),
                uri = image,
                isLayerResource = true
            )
        )
        bitmapImageLoadingWorkerJob?.get()?.start()
    }

    fun addSticker(@DrawableRes image: Int) {
        addSticker(BitmapFactory.decodeResource(resources, image))
    }

    private fun addSticker(image: Bitmap) = binding?.run {
        motionView.post(Runnable {
            val entity = ImageEntity(Layer(), image, motionView.width, motionView.height)
            motionView.addEntityAndPosition(entity)
        })
    }
}