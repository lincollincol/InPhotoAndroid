package com.linc.inphoto.utils.extensions.view

import android.graphics.Bitmap
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.linc.inphoto.entity.AspectRatio
import com.linc.inphoto.ui.cropimage.model.CropShape

fun CropImageView.setAspectRatio(
    aspectRatio: AspectRatio?
) = aspectRatio?.let {
    setAspectRatio(it.width.toInt(), it.height.toInt())
}

fun CropImageView.setCropShape(
    cropShape: CropShape?,
    cornerShape: CropImageView.CropCornerShape = CropImageView.CropCornerShape.RECTANGLE,
) = cropShape?.let {
    this.cropShape = when (cropShape) {
        is CropShape.Circle -> CropImageView.CropShape.OVAL
        else -> CropImageView.CropShape.RECTANGLE
    }
    this.cornerShape = cornerShape
}

fun CropImageView.CropResult.getCroppedBitmap(view: CropImageView): Bitmap? {
    val bitmap = getBitmap(view.context) ?: return null
    return when (view.cropShape) {
        CropImageView.CropShape.OVAL -> CropImage.toOvalBitmap(bitmap)
        else -> bitmap
    }
}