package com.linc.inphoto.ui.postcomments

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.postcomments.model.CommentUiState
import com.linc.inphoto.ui.postcomments.model.PostInfoUiState

data class PostCommentsUiState(
    val postInfoUiState: PostInfoUiState? = null,
    val comments: List<CommentUiState> = listOf(),
) : UiState