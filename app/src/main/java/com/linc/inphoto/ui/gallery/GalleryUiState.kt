package com.linc.inphoto.ui.gallery

import com.linc.inphoto.entity.LocalMedia
import com.linc.inphoto.ui.base.state.UiState

data class GalleryUiState(
    val images: List<LocalMedia> = emptyList()
) : UiState