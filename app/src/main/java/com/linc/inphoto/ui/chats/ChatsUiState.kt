package com.linc.inphoto.ui.chats

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.chats.model.ChatContactUiState
import com.linc.inphoto.ui.chats.model.ConversationUiState

data class ChatsUiState(
    val isLoading: Boolean = false,
    val chats: List<ConversationUiState> = listOf(),
    val contacts: List<ChatContactUiState> = listOf()
) : UiState