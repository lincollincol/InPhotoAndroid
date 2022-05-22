package com.linc.inphoto.ui.home

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.home.model.HomePostUiState
import com.linc.inphoto.ui.home.model.UserStoryUiState

data class HomeUiState(
    val posts: List<HomePostUiState> = listOf(),
    val stories: List<UserStoryUiState> = listOf(),
    val newStory: UserStoryUiState? = null,
    val isLoading: Boolean = false
) : UiState