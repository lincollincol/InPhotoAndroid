package com.linc.inphoto.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followers")
data class FollowerEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val followerId: String
)