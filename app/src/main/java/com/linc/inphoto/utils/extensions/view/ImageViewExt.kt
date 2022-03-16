package com.linc.inphoto.utils.extensions.view

import android.content.res.ColorStateList
import android.net.Uri
import android.util.Size
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.linc.inphoto.R
import com.linc.inphoto.utils.extensions.getDrawable
import jp.wasabeef.glide.transformations.BlurTransformation

private const val THUMB_MIN_SIZE = 56

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
        skipMemoryCache
    )
}

fun ImageView.loadImage(
    image: Any?,
    size: Size? = null,
    blurRadius: Int? = null,
//    @DrawableRes placeholder: Int? = R.drawable.ic_image,
    @DrawableRes errorPlaceholder: Int? = R.drawable.ic_broken_image,
    @ColorInt placeholderTint: Int? = null,
    @ColorInt errorTint: Int? = null,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    skipMemoryCache: Boolean = true
) {

    val circularProgressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }

    var requestOptions = RequestOptions()
//        .placeholder(context.getDrawable(placeholder, placeholderTint))
        .placeholder(circularProgressDrawable)
        .error(context.getDrawable(errorPlaceholder, errorTint))
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
        .apply(requestOptions)
        .into(this)
}

fun ImageView.setTint(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}
