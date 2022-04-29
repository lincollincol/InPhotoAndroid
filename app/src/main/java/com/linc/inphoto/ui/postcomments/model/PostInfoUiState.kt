package com.linc.inphoto.ui.postcomments.model

import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.state.UiState

data class PostInfoUiState(
    val createdTimestamp: Long,
    val description: String,
    val username: String,
    val userAvatarUrl: String?,
    val tags: List<String>,
    val onUserClicked: () -> Unit
) : UiState

fun ExtendedPost.toUiState(onUserClicked: () -> Unit) = PostInfoUiState(
    createdTimestamp = createdTimestamp,
    description = description,
    username = username,
    userAvatarUrl = userAvatarUrl,
    tags = tags,
    onUserClicked = onUserClicked
)