package com.linc.inphoto.data.dto.network

data class BaseResponse<T>(
    val success: Boolean,
    val body: T?
)