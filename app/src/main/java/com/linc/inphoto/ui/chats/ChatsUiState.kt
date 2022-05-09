package com.linc.inphoto.ui.chats

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.chats.model.ChatEntityUiState

data class ChatsUiState(
    val isLoading: Boolean = false,
    val chats: List<ChatEntityUiState> = listOf()
) : UiState