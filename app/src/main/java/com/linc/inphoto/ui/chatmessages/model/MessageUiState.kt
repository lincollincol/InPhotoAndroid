package com.linc.inphoto.ui.chatmessages.model

import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.utils.extensions.EMPTY

data class MessageUiState(
    val id: String,
    val text: String,
    val files: List<String>,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isSystem: Boolean,
    val isPending: Boolean,
    val onClick: () -> Unit,
    val onImageClick: () -> Unit
) : UiState {
    constructor(
        text: String,
        files: List<String>
    ) : this(
        id = String.EMPTY,
        text = text,
        files = files,
        createdTimestamp = System.currentTimeMillis(),
        isIncoming = false,
        isSystem = false,
        isPending = true,
        onClick = { /* Not implemented */ },
        onImageClick = { /* Not implemented */ }
    )
}

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
    isPending = false,
    onClick = onClick,
    onImageClick = onImageClick,
)