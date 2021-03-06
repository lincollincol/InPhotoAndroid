package com.linc.inphoto.entity.chat

data class Chat(
    val id: String,
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val lastMessage: String?,
    val lastMessageTimestamp: Long,
    val lastMessageFilesCount: Int
)