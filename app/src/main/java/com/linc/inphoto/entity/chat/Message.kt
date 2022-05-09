package com.linc.inphoto.entity.chat

data class Message(
    val id: String,
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val text: String,
    val files: List<String>,
    val createdTimestamp: Long,
)