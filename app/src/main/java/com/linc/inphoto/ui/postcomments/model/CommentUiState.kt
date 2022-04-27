package com.linc.inphoto.ui.postcomments.model

import com.linc.inphoto.entity.post.Comment
import com.linc.inphoto.ui.base.state.UiState

data class CommentUiState(
    val commentId: String,
    val createdTimestamp: Long,
    val comment: String,
    val username: String,
    val userAvatarUrl: String?,
    val onUserClicked: () -> Unit,
    val onCommentClicked: () -> Unit,
) : UiState

fun Comment.toUiState(
    onUserClicked: () -> Unit,
    onCommentClicked: () -> Unit
) = CommentUiState(
    commentId = id,
    createdTimestamp = createdTimestamp,
    comment = comment,
    username = username,
    userAvatarUrl = userAvatarUrl,
    onUserClicked = onUserClicked,
    onCommentClicked = onCommentClicked
)