package com.linc.inphoto.ui.imagesticker.model

import android.net.Uri
import com.linc.inphoto.entity.media.image.ImageSticker
import com.linc.inphoto.ui.base.state.ItemUiState

data class StickerUiState(
    val id: String,
    val uri: Uri,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = id.hashCode().toLong()
}

fun ImageSticker.toUiState(
    onClick: () -> Unit
) = StickerUiState(
    id = id,
    uri = uri,
    onClick = onClick
)