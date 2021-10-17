package com.linc.inphoto.data.api.dto.auth

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String
)
