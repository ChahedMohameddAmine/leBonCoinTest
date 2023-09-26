package com.leboncoin.datalayer.album.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.leboncoin.datalayer.album.local.baseDAO.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao : BaseDao<AlbumEntity> {


    @Query("SELECT (SELECT Count(*) FROM AlbumEntity LIMIT 1) < 1")
    fun isEmpty(): Boolean

    @Query("SELECT * FROM AlbumEntity")
    fun getAlbums(): Flow<List<AlbumEntity>>
}