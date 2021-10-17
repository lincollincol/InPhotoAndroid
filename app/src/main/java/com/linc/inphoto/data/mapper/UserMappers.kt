package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.api.dto.user.UserResponse
import com.linc.inphoto.data.storage.database.entity.UserEntity

fun UserResponse.toUserEntity() = UserEntity(
    id, name, email, status, publicProfile, avatarId
)