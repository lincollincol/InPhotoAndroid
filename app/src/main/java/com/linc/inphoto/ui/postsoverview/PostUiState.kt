package com.linc.inphoto.ui.postsoverview

import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.state.UiState

data class PostUiState(
    val post: ExtendedPost,
    val onLike: () -> Unit,
    val onBookmark: () -> Unit,
    val onComment: () -> Unit
) : UiState

fun ExtendedPost.toUiState(
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onComment: () -> Unit
) = PostUiState(this, onLike, onBookmark, onComment)