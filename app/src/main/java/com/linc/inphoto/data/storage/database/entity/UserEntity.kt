package com.linc.inphoto.data.storage.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val status: String?,
    @ColumnInfo(name = "is_public")
    val publicProfile: Boolean,
    @ColumnInfo(name = "avatar_url")
    val avatarId: String?
)