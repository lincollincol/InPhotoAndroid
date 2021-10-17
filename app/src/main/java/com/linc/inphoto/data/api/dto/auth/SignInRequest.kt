package com.linc.inphoto.data.api.dto.auth

data class SignInRequest(
    val email: String,
    val password: String
)