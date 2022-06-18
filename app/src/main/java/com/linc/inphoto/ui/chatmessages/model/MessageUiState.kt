package com.linc.inphoto.ui.chatmessages.model

import com.linc.inphoto.entity.chat.Attachment
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.ui.base.state.ItemUiState

data class MessageUiState(
    val id: String,
    val text: String,
    val attachments: List<Attachment>,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isSystem: Boolean,
    val isEdited: Boolean,
    val isProcessing: Boolean,
    val onClick: () -> Unit,
    val onImageClick: () -> Unit
) : ItemUiState {
    companion object {
        @JvmStatic
        fun getPendingMessageInstance(
            messageId: String,
            text: String,
            attachments: List<Attachment>
        ) = MessageUiState(
            id = messageId,
            text = text,
            attachments = attachments,
            createdTimestamp = System.currentTimeMillis(),
            isIncoming = false,
            isSystem = false,
            isProcessing = true,
            isEdited = false,
            onClick = { /* Not implemented */ },
            onImageClick = { /* Not implemented */ }
        )
    }

    override fun getStateItemId(): Long {
        return id.hashCode().toLong()
    }
}

val MessageUiState.hasAttachments get() = attachments.isNotEmpty()
val MessageUiState.hasSingleAttachment get() = attachments.count() == 1
val MessageUiState.hasMultipleAttachments get() = attachments.count() > 1
val MessageUiState.isTextOnlyMessage get() = text.isNotEmpty() && !hasAttachments

fun Message.toUiState(
    onClick: () -> Unit,
    onImageClick: () -> Unit
) = MessageUiState(
    id = id,
    text = text,
    attachments = attachments,
    createdTimestamp = createdTimestamp,
    isIncoming = isIncoming,
    isSystem = isSystem,
    isProcessing = false,
    isEdited = isEdited,
    onClick = onClick,
    onImageClick = onImageClick,
)