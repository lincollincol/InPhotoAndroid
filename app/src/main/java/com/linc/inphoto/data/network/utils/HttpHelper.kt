package com.rhythmoya.data.network.helper

object HttpHelper {

    private const val BEARER = "Bearer"

    object Header {
        const val AUTHORIZATION = "Authorization"
    }

    object Code {
        const val UNAUTHORIZED = 401
    }

    fun bearer(token: String) = "$BEARER $token"
}