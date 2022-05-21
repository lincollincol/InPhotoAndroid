package com.linc.inphoto.ui.profile.model

import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.ui.base.state.ItemUiState

data class ProfilePostUiState(
    val post: Post,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = post.id.hashCode().toLong()
}

fun Post.toUiState(onClick: () -> Unit) = ProfilePostUiState(this, onClick)