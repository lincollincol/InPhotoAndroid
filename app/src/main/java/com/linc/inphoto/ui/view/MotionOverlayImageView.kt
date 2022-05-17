package com.linc.inphoto.ui.view

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import com.linc.inphoto.databinding.LayoutMotionOverlayImageViewBinding
import com.linc.inphoto.entity.media.image.ImageSticker
import com.linc.inphoto.ui.view.model.ImageEntity
import com.linc.inphoto.ui.view.model.Layer
import com.linc.inphoto.ui.view.model.MotionEntity
import com.linc.inphoto.ui.view.model.TextEntity
import com.linc.inphoto.utils.extensions.inflater
import com.linc.inphoto.utils.extensions.update
import com.linc.inphoto.utils.extensions.view.loadImage
import kotlinx.coroutines.*
import java.lang.Runnable

class MotionOverlayImageView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), MotionView.MotionViewCallback, LifecycleObserver {

    private var binding: LayoutMotionOverlayImageViewBinding? = null
    private var coroutineScope: CoroutineScope? = null
    private val stickers: MutableList<ImageSticker> = mutableListOf()

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
//            motionView.deleteEntity(entity)
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

    fun updateStickers(newStickers: List<ImageSticker>) {
        newStickers.filter { !stickers.contains(it) }
            .forEach { addSticker(it.uri) }
        stickers.update(newStickers)
    }

    fun registerLifecycleOwner(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        coroutineScope = lifecycle.coroutineScope
    }

    fun unregisterLifecycleOwner(lifecycle: Lifecycle) {
        lifecycle.removeObserver(this)
        coroutineScope = null
    }

    fun setImageUri(image: Uri?) = binding?.run {
        imageView.loadImage(image)
    }

    fun addSticker(stickerUri: Uri) = binding?.run {
        coroutineScope?.launch(Dispatchers.IO) {
            ensureActive()
            val bitmapFuture = Glide.with(context).asBitmap().load(stickerUri).submit()
            val bitmap = bitmapFuture.get()
            mainMotionView.post(Runnable {
                val entity = ImageEntity(
                    Layer(),
                    bitmap,
                    mainMotionView.width,
                    mainMotionView.height
                )
                mainMotionView.addEntityAndPosition(entity)
            })
            Glide.with(context).clear(bitmapFuture)
        }
    }

    fun addSticker(stickerResId: Int) = binding?.run {
        mainMotionView.post(Runnable {
            val pica = BitmapFactory.decodeResource(resources, stickerResId)
            val entity = ImageEntity(Layer(), pica, mainMotionView.width, mainMotionView.height)
            mainMotionView.addEntityAndPosition(entity)
        })
    }
}