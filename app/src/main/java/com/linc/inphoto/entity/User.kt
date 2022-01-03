package com.linc.inphoto.entity

data class User(
    val id: String,
    val name: String?,
    val email: String,
    val status: String?,
    val publicProfile: Boolean,
    val avatarId: String?
)