package com.linc.inphoto.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.linc.inphoto.data.database.entity.FollowerEntity

@Dao
abstract class FollowerDao : BaseDao<FollowerEntity>() {

    @Query("SELECT * FROM followers WHERE userId = :userId")
    abstract suspend fun loadFollowers(userId: String): List<FollowerEntity>

    @Query("SELECT * FROM followers WHERE userId = :userId AND followerId = :followerId")
    abstract suspend fun loadFollower(userId: String, followerId: String): FollowerEntity?

    @Query("DELETE FROM followers WHERE userId = :userId AND followerId = :followerId")
    abstract suspend fun deleteFollower(userId: String, followerId: String)

    @Query("DELETE FROM followers WHERE userId = :userId")
    abstract suspend fun deleteUserFollowing(userId: String)

    @Query("DELETE FROM followers")
    abstract suspend fun deleteAll()

}