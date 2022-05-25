package com.linc.inphoto.ui.home.model

import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.state.ItemUiState

data class HomePostUiState(
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
    val onImage: () -> Unit,
    val onProfile: () -> Unit,
    val onMore: () -> Unit,
    val onDoubleTap: () -> Unit,
    val onLike: () -> Unit,
    val onBookmark: () -> Unit,
    val onComment: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = postId.hashCode().toLong()
}

fun ExtendedPost.toUiState(
    onImage: () -> Unit,
    onProfile: () -> Unit,
    onMore: () -> Unit,
    onDoubleTap: () -> Unit,
    onLike: () -> Unit,
    onBookmark: () -> Unit,
    onComment: () -> Unit
) = HomePostUiState(
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
    onImage = onImage,
    onProfile = onProfile,
    onMore = onMore,
    onDoubleTap = onDoubleTap,
    onLike = onLike,
    onBookmark = onBookmark,
    onComment = onComment
)