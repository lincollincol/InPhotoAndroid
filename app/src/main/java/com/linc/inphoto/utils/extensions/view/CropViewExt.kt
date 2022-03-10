package com.linc.inphoto.utils.extensions.view

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