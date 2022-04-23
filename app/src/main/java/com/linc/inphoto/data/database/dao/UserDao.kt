package com.linc.inphoto.data.database.dao

import androidx.room.*
import com.linc.inphoto.data.database.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("UPDATE users SET name = :name WHERE id = :id")
    fun updateUserName(id: String, name: String)

    @Query("UPDATE users SET status = :status WHERE id = :id")
    fun updateUserStatus(id: String, status: String)

    @Query("UPDATE users SET gender = :gender WHERE id = :id")
    fun updateUserGender(id: String, gender: String)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteUsers()

}