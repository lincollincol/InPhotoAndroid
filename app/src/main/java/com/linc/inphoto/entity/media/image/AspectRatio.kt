package com.linc.inphoto.entity.media.image

data class AspectRatio(
    val width: Float,
    val height: Float
) {
    val isSquare get() = width == height
    val ratio get() = width / height

}