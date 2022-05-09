package com.linc.inphoto.ui.chats.model

import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.ui.base.state.UiState

data class ChatEntityUiState(
    val avatarUrl: String,
    val username: String,
    val lastMessage: String,
    val lastMessageTimestamp: Long,
    val onClick: () -> Unit
) : UiState

fun Chat.toUiState(onClick: () -> Unit) = ChatEntityUiState(
    avatarUrl = userAvatarUrl,
    username = username,
    lastMessage = lastMessage,
    lastMessageTimestamp = lastMessageTimestamp,
    onClick = onClick
)