package com.linc.inphoto.ui.home

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.home.model.HomePostUiState
import com.linc.inphoto.ui.home.model.StoriesUiState

data class HomeUiState(
    val storiesUiState: StoriesUiState = StoriesUiState(),
    val posts: List<HomePostUiState> = listOf(),
    val isLoading: Boolean = false
) : UiState