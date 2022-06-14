package com.linc.inphoto.data.mapper

import com.linc.inphoto.data.network.model.post.CommentApiModel
import com.linc.inphoto.data.network.model.post.ExtendedPostApiModel
import com.linc.inphoto.data.network.model.post.PostApiModel
import com.linc.inphoto.data.network.model.post.TagApiModel
import com.linc.inphoto.entity.post.Comment
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.entity.post.Tag

fun ExtendedPostApiModel.toExtendedPostModel(
    isCurrentUserAuthor: Boolean
) = ExtendedPost(
    id = id,
    authorUserId = userId,
    createdTimestamp = createdTimestamp,
    description = description,
    contentUrl = contentUrl,
    username = username,
    userAvatarUrl = userAvatarUrl,
    isLiked = isLiked,
    isBookmarked = isBookmarked,
    isCurrentUserAuthor = isCurrentUserAuthor,
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

fun CommentApiModel.toCommentModel(isCurrentUserAuthor: Boolean) = Comment(
    id = id,
    comment = comment,
    createdTimestamp = createdTimestamp,
    userId = userId,
    username = username,
    userAvatarUrl = userAvatarUrl,
    isCurrentUserAuthor = isCurrentUserAuthor
)

fun TagApiModel.toTagModel() = Tag(id, name)