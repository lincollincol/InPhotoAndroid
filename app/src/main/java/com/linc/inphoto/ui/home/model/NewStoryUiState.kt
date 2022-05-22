package com.linc.inphoto.ui.home.model

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.ItemUiState

data class NewStoryUiState(
    val id: String,
    val userAvatarUrl: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = id.hashCode().toLong()
}

fun User.toUiState(onClick: () -> Unit) = NewStoryUiState(
    id = id,
    userAvatarUrl = avatarUrl,
    onClick = onClick
)