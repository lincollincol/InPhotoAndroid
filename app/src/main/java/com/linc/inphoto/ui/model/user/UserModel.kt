package com.linc.inphoto.ui.model.user

data class UserModel(
    val id: String,
    val name: String?,
    val email: String,
    val status: String?,
    val publicProfile: Boolean,
    val avatarId: String?
)