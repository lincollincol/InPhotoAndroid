package com.linc.inphoto.ui.tagposts.model

import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.ui.base.state.ItemUiState

data class TagPostUiState(
    val postId: String,
    val postContentUrl: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = postId.hashCode().toLong()
}

fun Post.toUiState(onClick: () -> Unit) = TagPostUiState(
    postId = id,
    postContentUrl = contentUrl,
    onClick = onClick
)