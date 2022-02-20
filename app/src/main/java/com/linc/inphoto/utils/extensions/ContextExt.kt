package com.linc.inphoto.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Insets
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


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