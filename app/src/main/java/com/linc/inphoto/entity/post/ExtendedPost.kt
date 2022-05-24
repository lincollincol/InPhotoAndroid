package com.linc.inphoto.entity.post

data class ExtendedPost(
    val id: String,
    val authorUserId: String,
    val createdTimestamp: Long,
    val description: String,
    val contentUrl: String,
    val username: String,
    val userAvatarUrl: String?,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val isCurrentUserAuthor: Boolean,
    val likesCount: Int,
    val commentsCount: Int,
    val tags: List<String>
)