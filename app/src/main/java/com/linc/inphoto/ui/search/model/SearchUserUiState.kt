package com.linc.inphoto.ui.search.model

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.ItemUiState

data class SearchUserUiState(
    val userId: String,
    val avatarUrl: String,
    val username: String,
    val status: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = userId.hashCode().toLong()
}

fun User.toUiState(onClick: () -> Unit) = SearchUserUiState(
    userId = id,
    avatarUrl = avatarUrl,
    username = name,
    status = status.orEmpty(),
    onClick = onClick
)