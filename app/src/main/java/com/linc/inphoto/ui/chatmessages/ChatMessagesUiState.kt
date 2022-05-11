package com.linc.inphoto.ui.chatmessages

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.chatmessages.model.MessageUiState

data class ChatMessagesUiState(
    val isLoading: Boolean = false,
    val isScrollDownOnUpdate: Boolean = false,
    val messages: List<MessageUiState> = listOf(),
    val message: String? = null,
    val editableMessageId: String? = null,
) : UiState

val ChatMessagesUiState.isMessageValid get() = !message.isNullOrEmpty()
val ChatMessagesUiState.isEditorState get() = !editableMessageId.isNullOrEmpty()