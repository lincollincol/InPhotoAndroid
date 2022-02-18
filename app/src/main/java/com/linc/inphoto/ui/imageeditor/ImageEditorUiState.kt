package com.linc.inphoto.ui.imageeditor

import com.linc.inphoto.ui.base.state.UiState

data class ImageEditorUiState(
    val ratioItems: List<RatioUiState> = listOf()
) : UiState