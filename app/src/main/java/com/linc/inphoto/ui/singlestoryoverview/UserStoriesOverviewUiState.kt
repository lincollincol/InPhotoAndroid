package com.linc.inphoto.ui.singlestoryoverview

import com.linc.inphoto.entity.story.UserStory
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.storiesoverview.model.StoryTurnType

data class UserStoriesOverviewUiState(
    val userStories: UserStory? = null,
    val storyPosition: Int = 0,
    val storyTurn: StoryTurnType? = null
) : UiState