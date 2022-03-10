package com.linc.inphoto.entity

data class AspectRatio(
    val width: Float,
    val height: Float
) {
    val isSquare get() = width == height
    val ratio get() = width / height

}