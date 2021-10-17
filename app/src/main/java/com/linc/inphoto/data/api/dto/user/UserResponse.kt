package com.linc.inphoto.data.api.dto.user

data class UserResponse(
    val id: String,
    val name: String?,
    val email: String,
    val status: String?,
    val publicProfile: Boolean,
    val accessToken: String,
    val avatarId: String?
)