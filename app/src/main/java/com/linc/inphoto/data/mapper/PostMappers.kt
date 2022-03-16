package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.entity.post.Post

fun ExtendedPostApiModel.toExtendedPostModel() = ExtendedPost(
    id = id,
    createdTimestamp = createdTimestamp,
    description = description,
    contentUrl = contentUrl,
    username = username,
    userAvatarUrl = userAvatarUrl,
    isLiked = isLiked,
    isBookmarked = isBookmarked,
    likesCount = likesCount,
    commentsCount = commentsCount,
    tags = tags
)

fun PostApiModel.toPostModel() = Post(
    id = id,
    createdTimestamp = createdTimestamp,
    description = description,
    contentUrl = contentUrl
)