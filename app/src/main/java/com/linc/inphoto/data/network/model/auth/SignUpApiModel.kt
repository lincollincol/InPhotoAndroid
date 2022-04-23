package com.linc.inphoto.data.network.model.auth

import com.linc.inphoto.entity.user.Gender

data class SignUpApiModel(
    val email: String,
    val username: String,
    val password: String,
    val gender: Gender
)
