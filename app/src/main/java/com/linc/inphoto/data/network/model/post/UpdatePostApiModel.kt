package com.linc.inphoto.data.network.model.post

data class UpdatePostApiModel(
    val description: String,
    val tags: List<String>
)