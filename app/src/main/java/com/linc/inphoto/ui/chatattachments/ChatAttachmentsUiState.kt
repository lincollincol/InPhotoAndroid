package com.linc.inphoto.ui.chatattachments

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.chatattachments.model.AttachmentUiState

data class ChatAttachmentsUiState(
    val attachments: List<AttachmentUiState> = listOf(),
    val captionText: String? = null,
    val isLoading: Boolean = false,
) : UiState