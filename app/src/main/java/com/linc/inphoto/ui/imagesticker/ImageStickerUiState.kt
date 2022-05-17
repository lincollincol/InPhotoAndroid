package com.linc.inphoto.ui.imagesticker

import android.net.Uri
import com.linc.inphoto.entity.media.image.ImageSticker
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.imagesticker.model.StickerUiState

data class ImageStickerUiState(
    val image: Uri? = null,
    val selectedStickers: List<ImageSticker> = listOf(),
    val availableStickers: List<StickerUiState> = listOf(),
) : UiState