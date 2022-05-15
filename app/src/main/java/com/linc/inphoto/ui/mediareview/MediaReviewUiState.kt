package com.linc.inphoto.ui.mediareview

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.mediareview.model.MediaFileUiState

data class MediaReviewUiState(
    val files: List<MediaFileUiState> = listOf()
) : UiState