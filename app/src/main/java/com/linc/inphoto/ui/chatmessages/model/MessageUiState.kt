package com.linc.inphoto.ui.chatmessages.model

import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.entity.media.RemoteMedia
import com.linc.inphoto.ui.base.state.ItemUiState
import com.linc.inphoto.utils.extensions.isAudioMimeType
import com.linc.inphoto.utils.extensions.isDocMimeType
import com.linc.inphoto.utils.extensions.isImageMimeType
import com.linc.inphoto.utils.extensions.isVideoMimeType

data class MessageUiState(
    val id: String,
    val text: String,
    val attachment: RemoteMedia?,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isEdited: Boolean,
    val isProcessing: Boolean,
    val isAudioPlaying: Boolean,
    val onClick: () -> Unit,
    val onImageClick: () -> Unit,
    val onAudioClick: () -> Unit
) : ItemUiState {
    companion object {
        @JvmStatic
        fun getPendingMessageInstance(
            messageId: String,
            text: String,
            attachment: RemoteMedia?
        ) = MessageUiState(
            id = messageId,
            text = text,
            attachment = attachment,
            createdTimestamp = System.currentTimeMillis(),
            isIncoming = false,
            isProcessing = true,
            isAudioPlaying = false,
            isEdited = false,
            onClick = { /* Not implemented */ },
            onImageClick = { /* Not implemented */ },
            onAudioClick = { /* Not implemented */ },
        )
    }

    override fun getStateItemId(): Long {
        return id.hashCode().toLong()
    }
}

val MessageUiState.isAudioMessage get() = hasAttachments && attachment?.mimeType.isAudioMimeType()
val MessageUiState.isImageMessage get() = hasAttachments && attachment?.mimeType.isImageMimeType()
val MessageUiState.isVideoMessage get() = hasAttachments && attachment?.mimeType.isVideoMimeType()
val MessageUiState.isDocumentMessage get() = hasAttachments && attachment?.mimeType.isDocMimeType()

val MessageUiState.hasAttachments get() = attachment != null
val MessageUiState.isTextOnlyMessage get() = text.isNotEmpty() && !hasAttachments

fun Message.toUiState(
    onClick: () -> Unit,
    onImageClick: () -> Unit,
    onAudioClick: () -> Unit
) = MessageUiState(
    id = id,
    text = text,
    attachment = attachments.firstOrNull(),
    createdTimestamp = createdTimestamp,
    isIncoming = isIncoming,
    isProcessing = false,
    isAudioPlaying = false,
    isEdited = isEdited,
    onClick = onClick,
    onImageClick = onImageClick,
    onAudioClick = onAudioClick
)