package com.linc.inphoto.ui.cropimage

import com.linc.inphoto.entity.media.image.AspectRatio
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.cropimage.model.CropShape

data class CropImageUiState(
    val ratioItems: List<RatioUiState> = listOf(),
    val currentRatio: AspectRatio? = null,
    val isFixedAspectRatio: Boolean = true,
    val cropShape: CropShape = CropShape.Rect()
) : UiState