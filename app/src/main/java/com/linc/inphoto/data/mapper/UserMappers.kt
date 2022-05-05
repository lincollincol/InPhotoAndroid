package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.database.entity.UserEntity
import com.linc.inphoto.data.network.model.user.UserApiModel
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.entity.user.User

fun UserApiModel.toUserEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    status = status,
    gender = gender.name,
    publicProfile = publicProfile,
    avatarUrl = avatarUrl,
    headerUrl = headerUrl,
    followersCount = followersCount,
    followingCount = followingCount
)

fun UserApiModel.toUserModel(
    isFollowingUser: Boolean,
    isLoggedInUser: Boolean
) = User(
    id = id,
    name = name,
    email = email,
    status = status,
    gender = gender,
    publicProfile = publicProfile,
    avatarUrl = avatarUrl,
    headerUrl = headerUrl,
    followersCount = followersCount,
    followingCount = followingCount,
    isFollowingUser = isFollowingUser,
    isLoggedInUser = isLoggedInUser
)

fun UserEntity.toUserModel(
    isFollowingUser: Boolean,
    isLoggedInUser: Boolean
) = User(
    id = id,
    name = name,
    email = email,
    status = status,
    gender = Gender.fromString(gender),
    publicProfile = publicProfile,
    avatarUrl = avatarUrl,
    headerUrl = headerUrl,
    followersCount = followersCount,
    followingCount = followingCount,
    isFollowingUser = isFollowingUser,
    isLoggedInUser = isLoggedInUser
)