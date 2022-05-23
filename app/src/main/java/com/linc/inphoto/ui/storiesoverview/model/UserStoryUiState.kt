package com.linc.inphoto.ui.storiesoverview.model

import com.linc.inphoto.entity.story.UserStory
import com.linc.inphoto.ui.base.state.ItemUiState

data class UserStoryUiState(val userId: String) : ItemUiState {
    override fun getStateItemId(): Long = userId.hashCode().toLong()
}

fun UserStory.toUiState() = UserStoryUiState(userId = userId)