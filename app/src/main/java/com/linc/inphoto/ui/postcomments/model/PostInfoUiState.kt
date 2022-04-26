package com.linc.inphoto.ui.postcomments.model

import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.state.UiState

data class PostInfoUiState(
    val createdTimestamp: Long,
    val description: String,
    val username: String,
    val userAvatarUrl: String?,
    val tags: List<String>
) : UiState

fun ExtendedPost.toUiState() = PostInfoUiState(
    createdTimestamp = createdTimestamp,
    description = description,
    username = username,
    userAvatarUrl = userAvatarUrl,
    tags = tags
)