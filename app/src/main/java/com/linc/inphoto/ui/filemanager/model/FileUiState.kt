package com.linc.inphoto.ui.filemanager.model

import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.base.state.ItemUiState
import com.linc.inphoto.utils.extensions.isAudioMimeType
import com.linc.inphoto.utils.extensions.isDocMimeType
import com.linc.inphoto.utils.extensions.isImageMimeType
import com.linc.inphoto.utils.extensions.isVideoMimeType

data class FileUiState(
    val localMedia: LocalMedia,
    val isSelected: Boolean,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long {
        return localMedia.uri.toString().hashCode().toLong()
    }
}

val FileUiState.isAudio: Boolean get() = localMedia.mimeType.isAudioMimeType()
val FileUiState.isImage: Boolean get() = localMedia.mimeType.isImageMimeType()
val FileUiState.isVideo: Boolean get() = localMedia.mimeType.isVideoMimeType()
val FileUiState.isDoc: Boolean get() = localMedia.mimeType.isDocMimeType()

fun LocalMedia.toUiState(onClick: () -> Unit) = FileUiState(
    localMedia = this,
    isSelected = false,
    onClick = onClick
)