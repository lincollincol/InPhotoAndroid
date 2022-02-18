package com.linc.inphoto.ui.imageeditor

data class RatioUiState(
    val width: Int,
    val height: Int,
    val onClick: () -> Unit,
    val selected: Boolean = false
)