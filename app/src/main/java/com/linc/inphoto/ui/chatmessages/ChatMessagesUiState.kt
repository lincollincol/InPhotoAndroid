package com.linc.inphoto.ui.chatmessages

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.chatmessages.model.MessageAttachmentUiState
import com.linc.inphoto.ui.chatmessages.model.MessageUiState

data class ChatMessagesUiState(
    val isLoading: Boolean = false,
    val isScrollDownOnUpdate: Boolean = false,
    val messages: List<MessageUiState> = listOf(),
    val editableMessageId: String? = null,
    val message: String? = null,
    val messageAttachments: List<MessageAttachmentUiState> = listOf(),
) : UiState

val ChatMessagesUiState.hasAttachments get() = messageAttachments.isNotEmpty()
val ChatMessagesUiState.isMessageValid get() = !message.isNullOrEmpty() || hasAttachments
val ChatMessagesUiState.isEditorState get() = !editableMessageId.isNullOrEmpty()