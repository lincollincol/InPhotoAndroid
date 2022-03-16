package com.linc.inphoto.data.network.model.post

data class PostApiModel(
    val id: String,
    val createdTimestamp: Long,
    val description: String,
    val contentUrl: String,
    val userId: String
)