package com.linc.inphoto.ui.postsoverview

import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.state.UiState

data class PostUiState(
    val postId: String,
    val authorUserId: String,
    val createdTimestamp: Long,
    val description: String,
    val contentUrl: String,
    val username: String,
    val userAvatarUrl: String?,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likesCount: Int,
    val commentsCount: Int,
    val tags: List<String>,
    val onDoubleTap: () -> Unit,
    val onLike: () -> Unit,
    val onBookmark: () -> Unit,
    val onComment: () -> Unit
) : UiState

fun ExtendedPost.toUiState(
    onDoubleTap: () -> Unit,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onComment: () -> Unit
) = PostUiState(
    postId = id,
    authorUserId = authorUserId,
    createdTimestamp = createdTimestamp,
    description = description,
    contentUrl = contentUrl,
    username = username,
    userAvatarUrl = userAvatarUrl,
    isLiked = isLiked,
    isBookmarked = isBookmarked,
    likesCount = likesCount,
    commentsCount = commentsCount,
    tags = tags,
    onDoubleTap,
    onLike,
    onBookmark,
    onComment
)