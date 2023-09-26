package com.leboncoin.datalayer.databasemanager

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leboncoin.datalayer.album.local.AlbumDao
import com.leboncoin.datalayer.album.local.AlbumEntity

@Database(entities = [AlbumEntity::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}