package com.linc.inphoto.utils.extensions.view

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Size
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.linc.inphoto.R
import jp.wasabeef.glide.transformations.BlurTransformation

private const val THUMB_MIN_SIZE = 56

const val IMAGE_BLUR_TINY = 5
const val IMAGE_BLUR_SMALL = 10
const val IMAGE_BLUR_MEDIUM = 15
const val IMAGE_BLUR_LARGE = 20

val THUMB_SMALL get() = Size(THUMB_MIN_SIZE, THUMB_MIN_SIZE)
val THUMB_MEDIUM get() = Size(THUMB_MIN_SIZE * 2, THUMB_MIN_SIZE * 2)
val THUMB_LARGE get() = Size(THUMB_MIN_SIZE * 4, THUMB_MIN_SIZE * 4)

val AVATAR_OPTIONS
    get() = RequestOptions()
        .placeholder(R.drawable.ic_person)
        .override(256)
        .error(R.drawable.ic_person)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)


fun ImageView.clearImage() {
    Glide.with(this).clear(this)
}

fun ImageView.loadUrlImage(
    url: String?,
    size: Size? = null,
    blurRadius: Int? = null,
    @DrawableRes placeholder: Int = R.drawable.ic_image,
    @DrawableRes errorPlaceholder: Int = R.drawable.ic_broken_image,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    skipMemoryCache: Boolean = true
) {
    loadImage(
        url,
        size,
        blurRadius,
//        placeholder,
        errorPlaceholder,
        null,
        null,
        diskCacheStrategy,
        skipMemoryCache
    )
}

fun ImageView.loadUriImage(
    uri: Uri?,
    size: Size? = null,
    blurRadius: Int? = null,
    @DrawableRes placeholder: Int = R.drawable.ic_image,
    @DrawableRes errorPlaceholder: Int = R.drawable.ic_broken_image,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    skipMemoryCache: Boolean = true
) {
    loadImage(
        uri,
        size,
        blurRadius,
//        placeholder,
        errorPlaceholder,
        null,
        null,
        diskCacheStrategy,
        skipMemoryCache,

        )
}

fun ImageView.loadImage(
    image: Any?,
    size: Size? = null,
    blurRadius: Int? = null,
//    @DrawableRes placeholder: Int? = R.drawable.ic_image,
    @DrawableRes errorPlaceholder: Int = R.drawable.ic_broken_image,
    @ColorInt placeholderTint: Int? = null,
    @ColorInt errorTint: Int? = null,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    skipMemoryCache: Boolean = false,
    reloadImage: Boolean = true,
    listener: RequestListener<Drawable>? = null
) {
    if (image == null) {
        return
    }

    if (!reloadImage && drawable != null) {
        return
    }

    var requestOptions = RequestOptions()
        .error(errorPlaceholder)
        .diskCacheStrategy(diskCacheStrategy)
        .skipMemoryCache(skipMemoryCache)
        .dontAnimate()
        .dontTransform()

    if (blurRadius != null) {
        requestOptions = requestOptions.apply(
            bitmapTransform(BlurTransformation(blurRadius))
        )
    }

    if (size != null) {
        requestOptions = requestOptions.override(size.width, size.height)
    }

    Glide.with(this)
        .load(image)
        .thumbnail(
            Glide.with(this)
                .load(image)
                .apply(bitmapTransform(BlurTransformation(24)))
                .override(48)
        )
        .listener(listener)
        .apply(requestOptions)
        .into(this)
}

fun ImageView.setTint(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun ImageView.bindWidthTo(view: View) = view.doOnLayout {
    updateLayoutParams { width = it.width }
}