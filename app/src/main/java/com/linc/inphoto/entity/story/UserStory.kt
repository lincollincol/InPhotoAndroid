package com.linc.inphoto.entity.story

data class UserStory(
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val latestStoryTimestamp: Long,
    val isLoggedInUser: Boolean,
    val stories: List<Story>
)