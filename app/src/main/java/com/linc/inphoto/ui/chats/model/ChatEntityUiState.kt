package com.linc.inphoto.ui.chats.model

import android.net.Uri
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.ui.base.state.UiState

data class ChatEntityUiState(
    val avatarUrl: String,
    val username: String,
    val lastMessage: String?,
    val lastMessageTimestamp: Long,
    val lastMessageFiles: List<Uri>,
    val onClick: () -> Unit
) : UiState

val ChatEntityUiState.isEmptyConversation
    get() =
        lastMessage == null

val ChatEntityUiState.isLastMessageTextOnly
    get() = !lastMessage.isNullOrEmpty() && lastMessageFiles.isEmpty()

val ChatEntityUiState.isLastMessageAttachmentsOnly get() = !isLastMessageTextOnly

fun Chat.toUiState(onClick: () -> Unit) = ChatEntityUiState(
    avatarUrl = userAvatarUrl,
    username = username,
    lastMessage = lastMessage,
    lastMessageTimestamp = lastMessageTimestamp,
    lastMessageFiles = lastMessageFiles,
    onClick = onClick
)