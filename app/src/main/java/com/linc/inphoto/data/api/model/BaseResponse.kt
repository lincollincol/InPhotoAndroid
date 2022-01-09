package com.linc.inphoto.data.api.model

data class BaseResponse<T>(
    val status: String,
    val body: T?
)