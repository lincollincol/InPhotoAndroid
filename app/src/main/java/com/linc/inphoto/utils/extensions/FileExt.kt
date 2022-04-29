package com.linc.inphoto.utils.extensions

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.toMultipartBody(name: String = this.name): MultipartBody.Part {
    val requestBody = asRequestBody("multipart/form-data".toMediaType())
    return MultipartBody.Part.createFormData(name, name, requestBody)
}