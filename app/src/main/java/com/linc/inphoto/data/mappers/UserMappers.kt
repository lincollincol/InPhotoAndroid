package com.linc.inphoto.data.mappers

import com.linc.inphoto.data.api.dto.user.UserResponse
import com.linc.inphoto.data.storage.database.entity.UserEntity
import com.linc.inphoto.ui.model.user.UserModel

fun UserResponse.toUserEntity() = UserEntity(
    id, name, email, status, publicProfile, avatarId
)

fun UserEntity.toUserModel() = UserModel(
    id, name, email, status, publicProfile, avatarId
)