package com.linc.inphoto.entity.post

data class Comment(
    val id: String,
    val comment: String,
    val createdTimestamp: Long,
    val userId: String,
    val username: String,
    val userAvatarUrl: String
)