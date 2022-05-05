package com.linc.inphoto.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.linc.inphoto.data.database.entity.FollowerEntity

@Dao
interface FollowerDao {

    @Query("SELECT * FROM followers WHERE userId = :userId")
    suspend fun loadFollowers(userId: String): List<FollowerEntity>

    @Query("SELECT * FROM followers WHERE userId = :userId AND followerId = :followerId")
    suspend fun loadFollower(userId: String, followerId: String): FollowerEntity?

    @Query("DELETE FROM followers WHERE userId = :userId AND followerId = :followerId")
    suspend fun deleteFollower(userId: String, followerId: String)

    @Insert
    suspend fun insertFollower(follower: FollowerEntity)

}