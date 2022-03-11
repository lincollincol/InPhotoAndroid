package com.linc.inphoto.ui.cropimage

import com.linc.inphoto.entity.media.image.AspectRatio

data class RatioUiState(
    val aspectRatio: AspectRatio,
    val onClick: () -> Unit,
    val selected: Boolean = false
)