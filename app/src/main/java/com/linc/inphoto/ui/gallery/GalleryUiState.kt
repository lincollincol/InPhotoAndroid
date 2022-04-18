package com.linc.inphoto.ui.gallery

import com.linc.inphoto.ui.base.state.UiState

data class GalleryUiState(
    val images: List<ImageUiState> = emptyList(),
    val galleryPermissionsGranted: Boolean = true
) : UiState