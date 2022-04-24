package com.linc.inphoto.ui.postcomments.model

import com.linc.inphoto.ui.base.state.UiState

data class CommentUiState(
    val commentId: String,
    val createdTimestamp: Long,
    val comment: String,
    val username: String,
    val userAvatarUrl: String?,
    val onUserClicked: () -> Unit
) : UiState