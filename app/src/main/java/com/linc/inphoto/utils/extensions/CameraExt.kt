package com.linc.inphoto.utils.extensions

import android.content.ContentResolver
import android.content.ContentValues
import android.content.res.Resources
import android.provider.MediaStore
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0

fun CameraProvider?.getCameraLens(isFrontCamera: Boolean): Int {
    return if (isFrontCamera && hasFrontCamera()) {
        CameraSelector.LENS_FACING_FRONT
    } else if (!isFrontCamera && hasBackCamera()) {
        CameraSelector.LENS_FACING_BACK
    } else {
        throw IllegalStateException("Desired lens is not unavailable")
    }
}

fun CameraProvider?.getFirstAvailableCameraLens(): Int {
    return when {
        hasBackCamera() -> CameraSelector.LENS_FACING_BACK
        hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
        else -> throw IllegalStateException("Back and front camera are unavailable")
    }
}

/** Returns true if the device has an available back camera. False otherwise */
fun CameraProvider?.hasBackCamera(): Boolean {
    return this?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
}

/** Returns true if the device has an available front camera. False otherwise */
fun CameraProvider?.hasFrontCamera(): Boolean {
    return this?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
}

fun aspectRatio(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val height = metrics.heightPixels
    val width = metrics.widthPixels
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
}

fun getOutputFileOptions(
    contentResolver: ContentResolver,
    fileName: String = UUID.randomUUID().toString()
): ImageCapture.OutputFileOptions {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
    }
    return ImageCapture.OutputFileOptions
        .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        .build()
}