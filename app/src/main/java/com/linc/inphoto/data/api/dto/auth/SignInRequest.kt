package com.linc.inphoto.data.api.dto.auth

data class SignInRequest(
    val login: String,
    val password: String
)