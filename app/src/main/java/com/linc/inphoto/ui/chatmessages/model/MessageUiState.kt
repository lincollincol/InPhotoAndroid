package com.linc.inphoto.ui.chatmessages.model

import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.ui.base.state.UiState

data class MessageUiState(
    val id: String,
    val text: String,
    val files: List<String>,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isSystem: Boolean,
    val onClick: () -> Unit,
    val onImageClick: () -> Unit
) : UiState

fun Message.toUiState(
    onClick: () -> Unit,
    onImageClick: () -> Unit
) = MessageUiState(
    id = id,
    text = text,
    files = files,
    createdTimestamp = createdTimestamp,
    isIncoming = isIncoming,
    isSystem = isSystem,
    onClick = onClick,
    onImageClick = onImageClick,
)