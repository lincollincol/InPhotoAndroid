package com.linc.inphoto.entity.chat

data class Message(
    val id: String,
    val userId: String,
    val text: String,
    val files: List<String>,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isSystem: Boolean,
    val isEdited: Boolean
)