package com.linc.inphoto.data.mapper

import android.net.Uri
import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import com.linc.inphoto.data.network.model.chat.MessageFirebaseModel
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.entity.user.User

fun ChatFirebaseModel.toModel(user: User?, message: MessageFirebaseModel) = Chat(
    id = id,
    userId = user?.id.orEmpty(),
    username = user?.name.orEmpty(),
    userAvatarUrl = user?.avatarUrl.orEmpty(),
    lastMessage = message.text,
    lastMessageTimestamp = message.createdTimestamp,
    lastMessageFiles = message.files.map(Uri::parse)
)

fun ChatFirebaseModel.toModel(user: User?, message: Message) = Chat(
    id = id,
    userId = user?.id.orEmpty(),
    username = user?.name.orEmpty(),
    userAvatarUrl = user?.avatarUrl.orEmpty(),
    lastMessage = message.text,
    lastMessageTimestamp = message.createdTimestamp,
    lastMessageFiles = message.files.map(Uri::parse)
)

fun MessageFirebaseModel.toModel(
    isIncoming: Boolean
) = Message(
    id = id,
    userId = userId,
    text = text,
    files = files,
    createdTimestamp = createdTimestamp,
    isSystem = isSystem,
    isEdited = isEdited,
    isIncoming = isIncoming
)