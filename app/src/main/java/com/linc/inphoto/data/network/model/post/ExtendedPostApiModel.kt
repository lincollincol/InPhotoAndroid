package com.linc.inphoto.data.network.model.post

data class ExtendedPostApiModel(
    val id: String,
    val createdTimestamp: Long,
    val description: String,
    val contentUrl: String,
    val username: String,
    val userAvatarUrl: String?,
    val userId: String,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likesCount: Int,
    val commentsCount: Int,
    val tags: List<String>
)