package com.linc.inphoto.data.network.model

data class BaseResponse<T>(
    val status: String,
    val failed: Boolean,
    val body: T?
)