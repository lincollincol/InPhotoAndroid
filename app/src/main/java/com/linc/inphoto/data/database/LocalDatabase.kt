package com.linc.inphoto.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.linc.inphoto.data.database.dao.FollowerDao
import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.database.entity.FollowerEntity
import com.linc.inphoto.data.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, FollowerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val followersDao: FollowerDao

    companion object {
        const val DATABASE_NAME = "InPhotoDB"
    }

}