package com.linc.inphoto.entity.post

data class Post(
    val id: String,
    val createdTimestamp: Long,
    val description: String,
    val contentUrl: String
)