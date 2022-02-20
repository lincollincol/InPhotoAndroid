package com.linc.inphoto.ui.imageeditor

import com.linc.inphoto.entity.AspectRatio
import com.linc.inphoto.ui.base.state.UiState

data class ImageEditorUiState(
    val ratioItems: List<RatioUiState> = listOf(),
    val currentRatio: AspectRatio? = null
) : UiState