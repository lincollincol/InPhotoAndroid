package com.linc.inphoto.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)

fun Context.screenViewportWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager()?.currentWindowMetrics ?: return 0
        val insets: Insets =
            windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager()?.defaultDisplay?.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

fun Context.screenViewportHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager()?.currentWindowMetrics ?: return 0
        val insets: Insets =
            windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.top - insets.bottom
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager()?.defaultDisplay?.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

fun Context.screenRawWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager()?.currentWindowMetrics ?: return 0
        windowMetrics.bounds.width()
    } else {
        val point = Point()
        @Suppress("DEPRECATION")
        windowManager()?.defaultDisplay?.getRealSize(point)
        point.x
    }
}

fun Context.screenRawHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager()?.currentWindowMetrics ?: return 0
        windowMetrics.bounds.height()
    } else {
        val point = Point()
        @Suppress("DEPRECATION")
        windowManager()?.defaultDisplay?.getRealSize(point)
        point.y
    }
}

fun Context?.getActivity(): Activity? {
    if (this == null) return null
    if (this is Activity) return this
    return if (this is ContextWrapper) baseContext.getActivity() else null
}

fun Context.windowManager(): WindowManager? =
    getSystemService(Context.WINDOW_SERVICE)?.safeCast<WindowManager>()

inline fun <reified T : Number> Context.getDimension(id: Int) =
    resources.getDimension(id).safeCast<T>()

fun Context.getKeyboard() =
    getSystemService(Context.INPUT_METHOD_SERVICE).safeCast<InputMethodManager>()


fun Context.getColorInt(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.getDrawable(
    @DrawableRes id: Int?,
    @ColorInt color: Int?
): Drawable? {
    id ?: return null
    return ContextCompat.getDrawable(this, id).also { drawable ->
        if (drawable != null && color != null) DrawableCompat.setTint(drawable, color)
    }
}

fun Context.createTempUri(): Uri {
    return createTempFile().toUri()
}

fun Context.createTempUri(bitmap: Bitmap): Uri {
    return createTempFile(bitmap).toUri()
}

fun Context.createTempFile(extension: String? = null): File {
    val temp = File(this.cacheDir, "${UUID.randomUUID()}${extension?.let { ".$it" }.orEmpty()}")
    temp.createNewFile()
    temp.deleteOnExit()
    return temp
}

fun Context.createTempFile(uri: Uri): File? {
    val data = uri.getFileBytes(this) ?: return null
    val extension = uri.getFileExtension(this)
    return createTempFile(extension).also { file -> file.writeBytes(data) }
}

fun Context.createTempFile(bitmap: Bitmap): File {
    val file = createTempFile()
    val outputStream = BufferedOutputStream(FileOutputStream(file))
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    return file
}

fun Context.deleteFileUri(uri: Uri?) = uri?.let {
    try {
        contentResolver.delete(it, null, null)
    } catch (iae: IllegalArgumentException) {
        // Unknown URL ignored
    }
}

fun Context.copyFileUri(src: Uri, dst: Uri) {
    val inputStream = contentResolver.openInputStream(src)
    val outputStream = contentResolver.openOutputStream(dst)
    val b = ByteArray(4096)
    var read: Int = -1
    while (inputStream?.read(b)?.also { read = it } != -1) {
        outputStream?.write(b, 0, read)
    }
    outputStream?.flush()
    outputStream?.close()
    inputStream.close()
}
