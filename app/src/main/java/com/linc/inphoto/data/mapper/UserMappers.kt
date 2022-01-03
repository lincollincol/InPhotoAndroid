package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.api.model.user.UserApiModel
import com.linc.inphoto.data.storage.database.entity.UserEntity
import com.linc.inphoto.entity.User

fun UserApiModel.toUserEntity() = UserEntity(
    id, name, email, status, publicProfile, avatarUrl
)

fun UserEntity.toUserModel() = User(
    id, name, email, status, publicProfile, avatarId
)