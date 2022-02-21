package com.linc.inphoto.utils.extensions

import com.steelkiwi.cropiwa.AspectRatio
import com.steelkiwi.cropiwa.config.CropIwaOverlayConfig
import com.steelkiwi.cropiwa.shape.CropIwaOvalShape
import com.steelkiwi.cropiwa.shape.CropIwaRectShape

fun CropIwaOverlayConfig.toggleShape(isRect: Boolean) =
    if (isRect) setRectCropShape() else setCircleCropShape()

fun CropIwaOverlayConfig.setCircleCropShape() = setCropShape(CropIwaOvalShape(this))

fun CropIwaOverlayConfig.setRectCropShape() = setCropShape(CropIwaRectShape(this))

fun CropIwaOverlayConfig.setAspectRatio(
    aspectRatio: com.linc.inphoto.entity.AspectRatio?
): CropIwaOverlayConfig {
    return aspectRatio?.let { setAspectRatio(it.width, it.height) } ?: this
}

fun CropIwaOverlayConfig.setAspectRatio(
    width: Float,
    height: Float
): CropIwaOverlayConfig = setAspectRatio(width.toInt(), height.toInt())

fun CropIwaOverlayConfig.setAspectRatio(
    width: Int,
    height: Int
): CropIwaOverlayConfig = setAspectRatio(AspectRatio(width, height))