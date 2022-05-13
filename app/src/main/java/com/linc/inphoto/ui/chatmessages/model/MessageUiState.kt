package com.linc.inphoto.ui.chatmessages.model

import android.net.Uri
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.utils.extensions.EMPTY

data class MessageUiState(
    val id: String,
    val text: String,
    val files: List<Uri>,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isSystem: Boolean,
    val isEdited: Boolean,
    val isProcessing: Boolean,
    val onClick: () -> Unit,
    val onImageClick: () -> Unit
) : UiState {
    constructor(
        text: String,
        files: List<Uri>
    ) : this(
        id = String.EMPTY,
        text = text,
        files = files,
        createdTimestamp = System.currentTimeMillis(),
        isIncoming = false,
        isSystem = false,
        isProcessing = true,
        isEdited = false,
        onClick = { /* Not implemented */ },
        onImageClick = { /* Not implemented */ }
    )
}

val MessageUiState.hasAttachments get() = files.isNotEmpty()
val MessageUiState.hasSingleAttachment get() = files.count() == 1
val MessageUiState.hasMultipleAttachments get() = files.count() > 1
val MessageUiState.isTextOnlyMessage get() = text.isNotEmpty() && !hasAttachments

fun Message.toUiState(
    onClick: () -> Unit,
    onImageClick: () -> Unit
) = MessageUiState(
    id = id,
    text = text,
    files = files.map(Uri::parse),
    createdTimestamp = createdTimestamp,
    isIncoming = isIncoming,
    isSystem = isSystem,
    isProcessing = false,
    isEdited = isEdited,
    onClick = onClick,
    onImageClick = onImageClick,
)