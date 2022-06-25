package com.linc.inphoto.utils.extensions.view

import android.content.res.ColorStateList
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Size
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.linc.inphoto.R
import com.linc.inphoto.utils.extensions.getStateListColor
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

fun ImageView.loadImage(
    image: Any?,
    size: Size? = null,
    blurRadius: Int? = null,
    scaleType: ImageView.ScaleType? = null,
    @DrawableRes errorPlaceholder: Int = R.drawable.ic_broken_image,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    skipMemoryCache: Boolean = false,
    reloadImage: Boolean = true,
    overrideOriginalSize: Boolean = false,
    crossFadeDurationMillis: Int = 150
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

    if (blurRadius != null) {
        requestOptions = requestOptions.apply(
            bitmapTransform(BlurTransformation(blurRadius))
        )
    }

    if (size != null) {
        requestOptions = requestOptions.override(size.width, size.height)
    }

    val factory = DrawableCrossFadeFactory.Builder(crossFadeDurationMillis)
        .setCrossFadeEnabled(true)
        .build()

    var builder = Glide.with(this)
        .load(image)
        .thumbnail(
            Glide.with(this)
                .load(image)
                .apply(bitmapTransform(BlurTransformation(24)))
                .transition(withCrossFade(factory))
                .override(48)
        )
        .transition(withCrossFade(factory))
        .apply(requestOptions)

    if (overrideOriginalSize) {
        builder = builder.override(Target.SIZE_ORIGINAL)
    }

    scaleType?.let(::setScaleType)

    builder.into(this)
}

fun ImageView.setTint(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun ImageView.setResTint(@ColorRes color: Int) {
    ImageViewCompat.setImageTintList(this, context.getStateListColor(color))
}

fun ImageView.getBitmapCoordinatesInsideImageView(): Rect {
    val rect = Rect()
    if (drawable == null) {
        return rect
    }

    // Get image dimensions
    // Get image matrix values and place them in an array
    val f = FloatArray(9)
    imageMatrix.getValues(f)

    // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
    val scaleX = f[Matrix.MSCALE_X]
    val scaleY = f[Matrix.MSCALE_Y]

    // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
    val d: Drawable = drawable
    val origW = d.intrinsicWidth
    val origH = d.intrinsicHeight

    // Calculate the actual dimensions
    val actW = Math.round(origW * scaleX)
    val actH = Math.round(origH * scaleY)

    // Get image position
    // We assume that the image is centered into ImageView
    val imgViewW: Int = width
    val imgViewH: Int = height
    rect.top = (imgViewH - actH) / 2
    rect.left = (imgViewW - actW) / 2
    rect.bottom = rect.top + actH
    rect.right = rect.left + actW
    return rect
}