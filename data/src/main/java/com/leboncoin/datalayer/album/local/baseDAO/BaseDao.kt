package com.leboncoin.datalayer.album.local.baseDAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update

@Dao
interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    suspend fun insert(obj: T): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertList(obj: List<T?>?): List<Long>

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)


}