package com.linc.inphoto.ui.chatmessages

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.chatmessages.model.MessageUiState

data class ChatMessagesUiState(
    val username: String? = null,
    val userAvatarUrl: String? = null,
    val messages: List<MessageUiState> = listOf(),
    val editableMessageId: String? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val isScrollDownOnUpdate: Boolean = false,
    val isHideKeyboard: Boolean = false
) : UiState

val ChatMessagesUiState.isMessageValid get() = !message.isNullOrEmpty()
val ChatMessagesUiState.isEditorState get() = !editableMessageId.isNullOrEmpty()