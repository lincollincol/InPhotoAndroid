package com.linc.inphoto.ui.webpage

import com.linc.inphoto.ui.base.state.UiState

data class WebPageUiState(
    val title: String? = null,
    val pageUrl: String? = null
) : UiState