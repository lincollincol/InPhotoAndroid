package com.linc.inphoto.data.network.model.story

data class StoryApiModel(
    val id: String,
    val contentUrl: String,
    val duration: Long,
    val createdTimestamp: Long,
    val expiresTimestamp: Long
)