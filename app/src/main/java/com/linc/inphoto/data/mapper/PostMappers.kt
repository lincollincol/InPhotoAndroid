package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.network.model.post.CommentApiModel
import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import com.linc.inphoto.entity.post.Comment
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.entity.post.Post

fun ExtendedPostApiModel.toExtendedPostModel() = ExtendedPost(
    id = id,
    authorUserId = userId,
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
    authorUserId = userId,
    createdTimestamp = createdTimestamp,
    description = description,
    contentUrl = contentUrl
)

fun CommentApiModel.toCommentModel() = Comment(
    id = id,
    comment = comment,
    createdTimestamp = createdTimestamp,
    userId = userId,
    username = username,
    userAvatarUrl = userAvatarUrl
)