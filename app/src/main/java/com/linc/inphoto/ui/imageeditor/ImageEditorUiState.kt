package com.linc.inphoto.ui.imageeditor

import com.linc.inphoto.entity.AspectRatio
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.imageeditor.model.CropShape

data class ImageEditorUiState(
    val ratioItems: List<RatioUiState> = listOf(),
    val currentRatio: AspectRatio? = null,
    val isDynamicOverlay: Boolean = false,
    val cropShape: CropShape = CropShape.Rect()
) : UiState