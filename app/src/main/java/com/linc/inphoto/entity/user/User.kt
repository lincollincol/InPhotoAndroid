package com.linc.inphoto.entity.user

data class User(
    val id: String,
    val name: String,
    val email: String,
    val status: String?,
    val gender: Gender,
    val publicProfile: Boolean,
    val avatarUrl: String,
    val headerUrl: String,
    val isLoggedInUser: Boolean
)