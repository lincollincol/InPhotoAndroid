package com.linc.inphoto.ui.profilefollowers.model

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.ItemUiState

data class FollowerUserUiState(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val status: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = userId.hashCode().toLong()
}

fun User.toUiState(onClick: () -> Unit) = FollowerUserUiState(
    userId = id,
    username = name,
    avatarUrl = avatarUrl,
    status = status.orEmpty(),
    onClick = onClick
)