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
import com.linc.inphoto.ui.view.imageoverlay.model.TextEntity
import com.linc.inphoto.utils.extensions.createTempUri
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
        if (entity is TextEntity) {
//                textEntityEditPanel.setVisibility(VISIBLE)
        } else {
//                textEntityEditPanel.setVisibility(GONE)
        }
    }

    override fun onEntityDoubleTap(entity: MotionEntity) {
//            startTextEntityEditing()
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

    fun setOnSaveImageListener(onSaveImageListener: (Uri?) -> Unit) {
        this.onSaveImageListener = onSaveImageListener
    }

    fun onImageLayersMergeAsyncComplete(result: BitmapLayerMergeWorkerJob.Result) {
        bitmapLayerMergeWorkerJob = null
        if (result.error != null) {
            Timber.e(result.error)
            return
        }
        binding?.motionView?.release()
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
        bitmapImageLoadingWorkerJob = null
        if (result.error != null) {
            Timber.e(result.error)
            return
        }
        binding?.imageView?.setImageBitmap(result.bitmap)
        bitmap = result.bitmap
    }

    fun saveImageAsync() = binding?.run {
        val bmp = bitmap ?: return@run
        val image = imageView.getBitmap() ?: return@run
        val layer = motionView.getBitmap(desiredWidth = image.width) ?: return@run

        val result = Bitmap.createBitmap(
            image.width,
            image.height,
            image.config
        )
        val canvas = Canvas(result)

        canvas.drawBitmap(image, 0F, 0F, null)
        canvas.drawBitmap(layer, 0F, 0F, null)

        // Crop layer
        val croppedLayerBitmap = Bitmap.createBitmap(
            result,
            0,
            (imageView.height - bmp.height) / 2,
            image.width,
            bmp.height
        )

        onSaveImageListener?.invoke(context.createTempUri(croppedLayerBitmap))
        /*bitmap ?: return
        val layersBitmap = binding?.motionView?.thumbnailImage ?: return

        bitmapLayerMergeWorkerJob?.get()?.cancel()
        bitmapLayerMergeWorkerJob = WeakReference(
            BitmapLayerMergeWorkerJob(
                context = context,
                overlayImageViewReference = WeakReference(this),
                imagePoint = Point(
                    binding?.imageView?.x?.toInt() ?: 0,
                    binding?.imageView?.y?.toInt() ?: 0
                ),
                bitmap!!,
                layersBitmap
            )
        )
        bitmapLayerMergeWorkerJob?.get()?.start()*/
    }

    fun setImageUri(image: Uri?) {
        image ?: return
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