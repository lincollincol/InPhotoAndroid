package com.linc.inphoto.utils.extensions.view

import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.linc.inphoto.entity.media.image.AspectRatio
import com.linc.inphoto.ui.cropimage.model.CropShape
import com.linc.inphoto.utils.extensions.copyUri
import com.linc.inphoto.utils.extensions.createTempUri

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

fun CropImageView.CropResult.applyShape(view: CropImageView) {
    uriContent ?: return
    val croppedBitmap = getBitmap(view.context) ?: return
    val shapeBitmap = when (view.cropShape) {
        CropImageView.CropShape.OVAL -> CropImage.toOvalBitmap(croppedBitmap)
        else -> croppedBitmap
    }
    with(view.context) {
        val shapeUri = createTempUri(shapeBitmap)
        copyUri(shapeUri, uriContent!!)
    }
}