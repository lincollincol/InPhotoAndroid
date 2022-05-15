package com.linc.inphoto.ui.chats.model

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.ItemUiState

data class ChatContactUiState(
    val userId: String,
    val avatarUrl: String,
    val username: String,
    val onClick: () -> Unit,
    val onUserClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long {
        return userId.hashCode().toLong()
    }
}

fun User.toUiState(
    onClick: () -> Unit,
    onUserClick: () -> Unit
) = ChatContactUiState(
    userId = id,
    avatarUrl = avatarUrl,
    username = name,
    onClick = onClick,
    onUserClick = onUserClick
)