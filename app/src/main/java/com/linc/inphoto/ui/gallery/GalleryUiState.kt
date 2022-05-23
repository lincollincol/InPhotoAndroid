package com.linc.inphoto.ui.gallery

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.gallery.model.ImageUiState

data class GalleryUiState(
    val images: List<ImageUiState> = emptyList(),
    val galleryPermissionsGranted: Boolean = true,
    val isLoading: Boolean = false
) : UiState