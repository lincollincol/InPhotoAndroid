package com.linc.inphoto.ui.home

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.home.model.HomePostUiState

data class HomeUiState(
    val posts: List<HomePostUiState> = emptyList()
) : UiState