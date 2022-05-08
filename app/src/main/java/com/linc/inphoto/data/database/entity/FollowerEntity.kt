package com.linc.inphoto.data.database.entity

import androidx.room.Entity

@Entity(tableName = "followers", primaryKeys = ["userId", "followerId"])
data class FollowerEntity(
    val userId: String,
    val followerId: String
)