package com.linc.inphoto.ui.view.imageoverlay

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.linc.inphoto.databinding.LayoutMotionOverlayImageViewBinding
import com.linc.inphoto.ui.view.imageoverlay.model.ImageEntity
import com.linc.inphoto.ui.view.imageoverlay.model.Layer
import com.linc.inphoto.ui.view.imageoverlay.model.MotionEntity
import com.linc.inphoto.ui.view.imageoverlay.model.TextEntity
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

    private var bitmapStickerLoadingWorkerJob: WeakReference<BitmapLoadingWorkerJob>? = null
    private var bitmapsMergingWorkerJob: WeakReference<BitmapsMergingWorkerJob>? = null

    init {
//        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTextView)
//        icon = attributes.getDrawable(R.styleable.ImageTextView_icon)
//        attributes.recycle()
    }

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
        binding?.mainMotionView?.deleteEntity(entity)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (binding != null) {
            return
        }

        binding = LayoutMotionOverlayImageViewBinding.inflate(context.inflater, this, true)
        binding?.run {
            mainMotionView.setMotionViewCallback(this@MotionOverlayImageView)

        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding = null
    }

    fun setOnSaveImageListener(onSaveImageListener: (Uri?) -> Unit) {
        this.onSaveImageListener = onSaveImageListener
    }

    fun onImageMergingAsyncComplete(result: BitmapsMergingWorkerJob.Result) {
        bitmapsMergingWorkerJob = null
        if (result.error != null) {
            Timber.e(result.error)
            return
        }
        binding?.mainMotionView?.release()
        onSaveImageListener?.invoke(result.uri)
    }

    fun onStickerLoadingAsyncComplete(result: BitmapLoadingWorkerJob.Result) {
        bitmapStickerLoadingWorkerJob = null
        if (result.error != null) {
            Timber.e(result.error)
            return
        }
        result.bitmap?.let(::addSticker)
    }

    fun saveImageAsync() {
        binding?.mainMotionView?.unselectEntity()
        bitmapsMergingWorkerJob?.get()?.cancel()
        bitmapsMergingWorkerJob = WeakReference(
            BitmapsMergingWorkerJob(
                context = context,
                overlayImageViewReference = WeakReference(this),
                motionViewReference = WeakReference(binding?.mainMotionView)
            )
        )
        bitmapsMergingWorkerJob?.get()?.start()
    }

    fun setImageUri(image: Uri?) = binding?.run {
        imageView.loadImage(image)
    }

    fun addStickerAsync(image: Uri) {
        bitmapStickerLoadingWorkerJob?.get()?.cancel()
        bitmapStickerLoadingWorkerJob = WeakReference(
            BitmapLoadingWorkerJob(
                overlayImageViewReference = WeakReference(this),
                uri = image
            )
        )
        bitmapStickerLoadingWorkerJob?.get()?.start()
    }

    fun addSticker(@DrawableRes image: Int) {
        addSticker(BitmapFactory.decodeResource(resources, image))
    }

    private fun addSticker(image: Bitmap) = binding?.run {
        mainMotionView.post(Runnable {
            val entity = ImageEntity(Layer(), image, mainMotionView.width, mainMotionView.height)
            mainMotionView.addEntityAndPosition(entity)
        })
    }
}