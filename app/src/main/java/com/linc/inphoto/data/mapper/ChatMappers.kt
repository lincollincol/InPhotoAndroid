package com.linc.inphoto.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import com.linc.inphoto.data.network.model.chat.MessageFirebaseModel
import com.linc.inphoto.data.network.model.media.RemoteMediaApiModel
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.utils.getList

fun ChatFirebaseModel.toModel(user: User?, message: MessageFirebaseModel?) = Chat(
    id = id,
    userId = user?.id.orEmpty(),
    username = user?.name.orEmpty(),
    userAvatarUrl = user?.avatarUrl.orEmpty(),
    lastMessage = message?.text,
    lastMessageTimestamp = message?.createdTimestamp ?: 0L,
    lastMessageFilesCount = message?.attachments?.count() ?: 0
)

fun ChatFirebaseModel.toModel(user: User?, message: Message) = Chat(
    id = id,
    userId = user?.id.orEmpty(),
    username = user?.name.orEmpty(),
    userAvatarUrl = user?.avatarUrl.orEmpty(),
    lastMessage = message.text,
    lastMessageTimestamp = message.createdTimestamp,
    lastMessageFilesCount = message.attachments.count()
)

fun MessageFirebaseModel.toModel(
    isIncoming: Boolean
) = Message(
    id = id,
    userId = userId,
    text = text,
    attachments = attachments.map(RemoteMediaApiModel::toModel),
    createdTimestamp = createdTimestamp,
    isEdited = isEdited,
    isIncoming = isIncoming
)

fun DocumentSnapshot.toMessageModel() = MessageFirebaseModel(
    userId = getField<String>("userId").orEmpty(),
    text = getField<String>("text").orEmpty(),
    attachments = getList<HashMap<String, Any>>("attachments")
        .map(HashMap<String, Any>::toRemoteMediaModel),
    createdTimestamp = getField("createdTimestamp") ?: 0L,
    isEdited = getField("edited") ?: false
).also { it.id = id }

fun DocumentSnapshot.toChatModel() =
    ChatFirebaseModel(participants = getList("participants")).also { it.id = id }

fun HashMap<String, Any>.toRemoteMediaModel() = RemoteMediaApiModel(
    get("url").toString(),
    get("name").toString(),
    get("mimeType").toString(),
    get("extension").toString(),
)