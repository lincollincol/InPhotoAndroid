package com.linc.inphoto.entity.chat

import android.net.Uri

data class Chat(
    val id: String,
    val userId: String,
    val username: String,
    val userAvatarUrl: String,
    val lastMessage: String?,
    val lastMessageTimestamp: Long,
    val lastMessageFiles: List<Uri>
)