package com.linc.inphoto.ui.userstories

import com.linc.inphoto.entity.story.Story
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.storiesoverview.model.StoryTurnType

data class UserStoriesUiState(
    val userId: String? = null,
    val username: String? = null,
    val userAvatarUrl: String? = null,
    val stories: List<Story> = listOf(),
    val storyPosition: Int = 0,
    val storyTurn: StoryTurnType? = null
) : UiState