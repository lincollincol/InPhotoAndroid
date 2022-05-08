package com.linc.inphoto.data.database.dao

import androidx.room.*


@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entity: List<T>): List<Long>

    @Update
    abstract suspend fun update(entity: T)

    @Update
    abstract suspend fun update(entity: List<T>)

    @Delete
    abstract suspend fun delete(entity: T)

    @Transaction
    open suspend fun upsert(entity: T) {
        val id = insert(entity)
        if (id == -1L) {
            update(entity)
        }
    }

    @Transaction
    open suspend fun upsert(entities: List<T>) {
        insert(entities).withIndex()
            .filter { it.value == -1L }
            .forEach { update(entities[it.index]) }
    }

}