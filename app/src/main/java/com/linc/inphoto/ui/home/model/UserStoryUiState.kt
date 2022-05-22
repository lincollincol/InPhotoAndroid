package com.linc.inphoto.ui.home.model

import com.linc.inphoto.entity.story.Story
import com.linc.inphoto.entity.story.UserStory
import com.linc.inphoto.ui.base.state.ItemUiState

data class UserStoryUiState(
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = userId.hashCode().toLong()
}

fun UserStory.toUiState(onClick: () -> Unit) = UserStoryUiState(
    userId = userId,
    username = username,
    userAvatarUrl = userAvatarUrl,
    onClick = onClick
)