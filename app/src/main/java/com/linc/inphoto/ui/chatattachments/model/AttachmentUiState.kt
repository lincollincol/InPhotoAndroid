package com.linc.inphoto.ui.chatattachments.model

import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.base.state.ItemUiState
import com.linc.inphoto.utils.extensions.isAudioMimeType
import com.linc.inphoto.utils.extensions.isDocMimeType
import com.linc.inphoto.utils.extensions.isImageMimeType
import com.linc.inphoto.utils.extensions.isVideoMimeType

data class AttachmentUiState(
    val localMedia: LocalMedia
) : ItemUiState {
    override fun getStateItemId(): Long = localMedia.uri.toString().hashCode().toLong()
}

val AttachmentUiState.isAudio: Boolean get() = localMedia.mimeType.isAudioMimeType()
val AttachmentUiState.isImage: Boolean get() = localMedia.mimeType.isImageMimeType()
val AttachmentUiState.isVideo: Boolean get() = localMedia.mimeType.isVideoMimeType()
val AttachmentUiState.isDoc: Boolean get() = localMedia.mimeType.isDocMimeType()

fun LocalMedia.toUiState() = AttachmentUiState(this)