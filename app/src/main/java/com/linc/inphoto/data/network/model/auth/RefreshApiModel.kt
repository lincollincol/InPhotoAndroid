package com.linc.inphoto.data.network.model.auth

data class RefreshApiModel(
    val accessToken: String,
    val refreshToken: String
)