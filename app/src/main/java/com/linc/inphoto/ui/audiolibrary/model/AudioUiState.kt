package com.linc.inphoto.ui.audiolibrary.model

import android.net.Uri
import com.linc.inphoto.entity.LocalMedia
import com.linc.inphoto.ui.base.state.ItemUiState

data class AudioUiState(
    val uri: Uri,
    val name: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long {
        return uri.toString().hashCode().toLong()
    }
}

fun LocalMedia.toUiState(onClick: () -> Unit) = AudioUiState(
    uri = uri,
    name = name,
    onClick = onClick
)