package com.linc.inphoto.ui.profile.model

import com.linc.inphoto.entity.post.Post

data class ProfilePostUiState(
    val post: Post,
    val onClick: () -> Unit
)

fun Post.toUiState(onClick: () -> Unit) = ProfilePostUiState(this, onClick)