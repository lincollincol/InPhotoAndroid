package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.entity.user.User

fun ChatFirebaseModel.toModel(user: User) = Chat(
    id = id,
    userId = user.id,
    username = user.name,
    userAvatarUrl = user.avatarUrl
)