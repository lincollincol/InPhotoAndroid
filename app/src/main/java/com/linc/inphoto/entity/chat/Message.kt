package com.linc.inphoto.entity.chat

import com.linc.inphoto.entity.media.RemoteMedia

data class Message(
    val id: String,
    val userId: String,
    val text: String,
    val attachments: List<RemoteMedia>,
    val createdTimestamp: Long,
    val isIncoming: Boolean,
    val isEdited: Boolean
)