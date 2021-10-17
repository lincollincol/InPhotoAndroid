package com.linc.inphoto.data.api.dto

data class BaseResponse<T>(
    val success: Boolean,
    val error: String? = null,
    val body: T?
)