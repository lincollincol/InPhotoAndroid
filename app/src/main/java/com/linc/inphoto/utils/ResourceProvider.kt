package com.linc.inphoto.utils

import android.app.Application
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val resources get() = context.resources
    private val assets get() = context.assets

    fun getAssetFileDescriptor(filename: String): AssetFileDescriptor =
        assets.openFd(filename)

    fun getFontFromAssets(filename: String): Typeface =
        Typeface.createFromAsset(context.assets, filename)

    fun getDimension(@DimenRes dimenRes: Int): Float =
        resources.getDimension(dimenRes)

    fun getDimensionPixelSize(@DimenRes dimenRes: Int): Int =
        resources.getDimensionPixelSize(dimenRes)

    fun getString(@StringRes stringId: Int, vararg args: Any? = arrayOf()): String =
        when (args.isEmpty()) {
            true -> resources.getString(stringId)
            else -> resources.getString(stringId, *args)
        }

    fun getPlural(@PluralsRes pluralId: Int, quantity: Int, vararg args: Any? = arrayOf()): String =
        when (args.isEmpty()) {
            true -> resources.getQuantityString(pluralId, quantity)
            else -> resources.getQuantityString(pluralId, quantity, *args)
        }

    fun getDrawable(@DrawableRes drawableId: Int): Drawable? =
        ContextCompat.getDrawable(context, drawableId)

    @ColorInt
    fun getColor(@ColorRes colorId: Int): Int =
        ContextCompat.getColor(context, colorId)

    fun getBitmap(@DrawableRes drawableId: Int): Bitmap? = with(getDrawable(drawableId)) {
        this ?: return@with null
        if (this is BitmapDrawable) return@with this.bitmap
        val bitmap = Bitmap.createBitmap(
            intrinsicWidth,
            intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        return@with bitmap
    }
}
