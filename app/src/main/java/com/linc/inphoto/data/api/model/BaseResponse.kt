package com.linc.inphoto.data.api.model

data class BaseResponse<T>(
    val success: Boolean,
    val error: String? = null,
    val body: T?
)