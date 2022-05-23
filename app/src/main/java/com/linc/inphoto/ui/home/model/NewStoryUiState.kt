package com.linc.inphoto.ui.home.model

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.ItemUiState

data class NewStoryUiState(
    val userAvatarUrl: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = javaClass.simpleName.hashCode().toLong()
}

fun User.toUiState(onClick: () -> Unit) = NewStoryUiState(
    userAvatarUrl = avatarUrl,
    onClick = onClick
)