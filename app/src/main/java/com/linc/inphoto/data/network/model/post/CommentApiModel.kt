package com.linc.inphoto.data.network.model.post

data class CommentApiModel(
    val id: String,
    val comment: String,
    val createdTimestamp: Long,
    val userId: String,
    val username: String,
    val userAvatarUrl: String
)