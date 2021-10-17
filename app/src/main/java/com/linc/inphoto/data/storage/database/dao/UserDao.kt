package com.linc.inphoto.data.storage.database.dao

import androidx.room.*
import com.linc.inphoto.data.storage.database.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String) : UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)

}