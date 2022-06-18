package com.linc.inphoto.data.mapper

import android.net.Uri
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import com.linc.inphoto.data.network.model.chat.AttachmentFirebaseModel
import com.linc.inphoto.data.network.model.chat.ChatFirebaseModel
import com.linc.inphoto.data.network.model.chat.MessageFirebaseModel
import com.linc.inphoto.entity.chat.Attachment
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.utils.getList
import kotlinx.coroutines.tasks.await

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
    attachments = attachments.mapNotNull(AttachmentFirebaseModel::toModel),
    createdTimestamp = createdTimestamp,
    isSystem = isSystem,
    isEdited = isEdited,
    isIncoming = isIncoming
)

fun AttachmentFirebaseModel.toModel() = Attachment(id, Uri.parse(url), type)

suspend fun DocumentSnapshot.toMessageModel() = MessageFirebaseModel(
    userId = getField<String>("userId").orEmpty(),
    text = getField<String>("text").orEmpty(),
    attachments = getList<DocumentReference>("attachments")
        .mapNotNull { it.get().await().toAttachmentModel() },
    createdTimestamp = getField("createdTimestamp") ?: 0L,
    isSystem = getField("system") ?: false,
    isEdited = getField("edited") ?: false
).also { it.id = id }

fun DocumentSnapshot.toAttachmentModel() = AttachmentFirebaseModel(
    url = getString("url").orEmpty(),
    type = getString("type").orEmpty()
).also { it.id = id }

fun DocumentSnapshot.toChatModel() =
    ChatFirebaseModel(participants = getList("participants")).also { it.id = id }