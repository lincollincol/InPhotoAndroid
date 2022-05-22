package com.linc.inphoto.data.network.model.story

data class UserStoryApiModel(
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val stories: List<StoryApiModel>
)