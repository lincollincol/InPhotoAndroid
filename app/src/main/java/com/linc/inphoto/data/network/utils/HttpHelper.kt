package com.rhythmoya.data.network.helper

object HttpHelper {

    private const val BEARER = "Bearer"

    object Header {
        const val AUTHORIZATION = "Authorization"
        const val MULTIPART_REQUEST = "Multipart: true"
    }

    object MediaType {
        const val MULTIPART_FORM_DATA = "multipart/form-data"
    }

    object Code {
        const val UNAUTHORIZED = 401
    }

    fun bearer(token: String) = "$BEARER $token"
}