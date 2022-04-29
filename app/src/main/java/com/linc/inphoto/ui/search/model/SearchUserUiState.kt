package com.linc.inphoto.ui.search.model

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.UiState

data class SearchUserUiState(
    val avatarUrl: String,
    val username: String,
    val status: String,
    val onClick: () -> Unit
) : UiState

fun User.toUiState(onClick: () -> Unit) = SearchUserUiState(
    avatarUrl = avatarUrl,
    username = name,
    status = status.orEmpty(),
    onClick = onClick
)