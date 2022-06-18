package com.linc.inphoto.entity.chat

data class Message(
    val id: String,
    val userId: String,
    val text: String,
    val attachments: List<Attachment>,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isSystem: Boolean,
    val isEdited: Boolean
)