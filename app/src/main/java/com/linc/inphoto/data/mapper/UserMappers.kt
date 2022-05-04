package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.database.entity.UserEntity
import com.linc.inphoto.data.network.model.user.UserApiModel
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.entity.user.User

fun UserApiModel.toUserEntity() = UserEntity(
    id, name, email, status, gender.name, publicProfile, avatarUrl, headerUrl
)

fun UserEntity.toUserModel(isLoggedInUser: Boolean) = User(
    id,
    name,
    email,
    status,
    Gender.fromString(gender),
    publicProfile,
    avatarUrl,
    headerUrl,
    isLoggedInUser
)