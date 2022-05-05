package com.linc.inphoto.data.network.model.user

import com.linc.inphoto.entity.user.Gender

data class UserApiModel(
    val id: String,
    val name: String,
    val email: String,
    val status: String?,
    val gender: Gender,
    val publicProfile: Boolean,
    val avatarUrl: String,
    val headerUrl: String,
    val accessToken: String,
    val followersCount: Int,
    val followingCount: Int
)