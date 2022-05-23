package com.linc.inphoto.ui.storiesoverview

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.storiesoverview.model.UserStoryUiState

data class StoriesOverviewUiState(
    val stories: List<UserStoryUiState> = listOf(),
    val singleStoryOverviewed: Boolean = false,
    val storyPosition: Int = 0,
    val isLoading: Boolean = false
) : UiState