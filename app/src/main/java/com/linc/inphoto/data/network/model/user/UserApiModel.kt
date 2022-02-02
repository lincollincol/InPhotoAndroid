package com.linc.inphoto.data.network.model.user

data class UserApiModel(
    val id: String,
    val name: String,
    val email: String,
    val status: String?,
    val publicProfile: Boolean,
    val avatarUrl: String?,
    val accessToken: String
)