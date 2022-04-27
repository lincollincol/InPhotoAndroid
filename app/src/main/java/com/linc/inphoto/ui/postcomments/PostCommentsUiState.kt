package com.linc.inphoto.ui.postcomments

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.postcomments.model.CommentUiState
import com.linc.inphoto.ui.postcomments.model.PostInfoUiState
import com.linc.inphoto.utils.collections.ImmutableDeque

data class PostCommentsUiState(
    val postInfoUiState: PostInfoUiState? = null,
    val comments: ImmutableDeque<CommentUiState> = ImmutableDeque(ArrayDeque()),
    val commentMessage: String? = null,
    val editableCommentId: String? = null
) : UiState

val PostCommentsUiState.isCommentValid get() = !commentMessage.isNullOrEmpty()
val PostCommentsUiState.isEditorState get() = !editableCommentId.isNullOrEmpty()