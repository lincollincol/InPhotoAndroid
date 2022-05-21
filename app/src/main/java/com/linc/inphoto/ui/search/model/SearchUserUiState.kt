package com.linc.inphoto.ui.search.model

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.ItemUiState

data class SearchUserUiState(
    val userId: String,
    val avatarUrl: String,
    val username: String,
    val status: String,
    val isFollowing: Boolean,
    val onClick: () -> Unit,
    val onFollow: () -> Unit,
) : ItemUiState {
    override fun getStateItemId(): Long = userId.hashCode().toLong()
}

fun User.toUiState(
    onClick: () -> Unit,
    onFollow: () -> Unit
) = SearchUserUiState(
    userId = id,
    avatarUrl = avatarUrl,
    username = name,
    status = status.orEmpty(),
    isFollowing = isFollowingUser,
    onClick = onClick,
    onFollow = onFollow,
)