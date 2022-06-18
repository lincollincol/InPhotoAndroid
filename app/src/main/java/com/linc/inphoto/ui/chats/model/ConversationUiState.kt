package com.linc.inphoto.ui.chats.model

import com.linc.inphoto.entity.chat.Chat
import com.linc.inphoto.ui.base.state.ItemUiState

data class ConversationUiState(
    val chatId: String,
    val userId: String,
    val avatarUrl: String,
    val username: String,
    val lastMessage: String?,
    val lastMessageTimestamp: Long,
    val lastMessageFilesCount: Int,
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
    get() = !lastMessage.isNullOrEmpty() && lastMessageFilesCount == 0

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
    lastMessageFilesCount = lastMessageFilesCount,
    onClick = onClick,
    onMenuClick = onMenuClick,
    onUserClick = onUserClick
)