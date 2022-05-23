package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.network.model.story.StoryApiModel
import com.linc.inphoto.data.network.model.story.UserStoryApiModel
import com.linc.inphoto.entity.story.Story
import com.linc.inphoto.entity.story.UserStory

fun StoryApiModel.toModel() = Story(
    id = id,
    contentUrl = contentUrl,
    duration = duration,
    createdTimestamp = createdTimestamp,
    expiresTimestamp = expiresTimestamp
)

fun UserStoryApiModel.toModel(isLoggedInUser: Boolean) = UserStory(
    userId = userId,
    username = username,
    userAvatarUrl = userAvatarUrl,
    latestStoryTimestamp = latestStoryTimestamp,
    isLoggedInUser = isLoggedInUser,
    stories = stories.map(StoryApiModel::toModel)
)