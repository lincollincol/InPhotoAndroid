package com.linc.inphoto.ui.mediareview.model

import com.linc.inphoto.entity.media.Media
import com.linc.inphoto.ui.base.state.ItemUiState

data class MediaFileUiState(
    val media: Media
) : ItemUiState {
    override fun getStateItemId(): Long = media.uri.toString().hashCode().toLong()
}