package com.linc.inphoto.ui.audiolibrary.model

import com.linc.inphoto.entity.LocalMedia
import com.linc.inphoto.ui.base.state.ItemUiState

data class AudioUiState(
    val localMedia: LocalMedia,
    val isSelected: Boolean,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long {
        return localMedia.uri.toString().hashCode().toLong()
    }
}

fun LocalMedia.toUiState(onClick: () -> Unit) = AudioUiState(
    localMedia = this,
    isSelected = false,
    onClick = onClick
)