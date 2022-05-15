package com.linc.inphoto.ui.chats.model

import android.net.Uri
import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.ui.base.state.ItemUiState

data class ConversationUiState(
    val chatId: String,
    val userId: String,
    val avatarUrl: String,
    val username: String,
    val lastMessage: String?,
    val lastMessageTimestamp: Long,
    val lastMessageFiles: List<Uri>,
    val onClick: () -> Unit,
    val onMenuClick: () -> Unit,
    val onUserClick: () -> Unit,
) : ItemUiState {
    override fun getStateItemId(): Long {
        return chatId.hashCode().toLong()
    }
}

val ConversationUiState.isEmptyConversation
    get() = lastMessage == null

val ConversationUiState.isLastMessageTextOnly
    get() = !lastMessage.isNullOrEmpty() && lastMessageFiles.isEmpty()

val ConversationUiState.isLastMessageAttachmentsOnly get() = !isLastMessageTextOnly

fun Chat.toUiState(
    onClick: () -> Unit,
    onMenuClick: () -> Unit,
    onUserClick: () -> Unit
) = ConversationUiState(
    chatId = id,
    userId = userId,
    avatarUrl = userAvatarUrl,
    username = username,
    lastMessage = lastMessage,
    lastMessageTimestamp = lastMessageTimestamp,
    lastMessageFiles = lastMessageFiles,
    onClick = onClick,
    onMenuClick = onMenuClick,
    onUserClick = onUserClick
)