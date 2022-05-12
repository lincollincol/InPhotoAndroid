package com.linc.inphoto.data.network.model.chat

data class MessageFirebaseModel(
    val id: String,
    val userId: String,
    val text: String,
    val files: List<String>,
    val createdTimestamp: Long,
    val isSystem: Boolean,
    val isUpdated: Boolean
)