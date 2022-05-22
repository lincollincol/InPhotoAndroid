package com.linc.inphoto.entity.story

data class Story(
    val id: String,
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val contentUrl: String,
    val duration: Long,
    val createdTimestamp: Long,
    val expiresTimestamp: Long
)