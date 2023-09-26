package com.leboncoin.datalayer.di

import android.content.Context
import androidx.room.Room
import com.leboncoin.datalayer.album.local.AlbumDao
import com.leboncoin.datalayer.databasemanager.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideAlbumDao(appDatabase: AppDatabase): AlbumDao {
        return appDatabase.albumDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATA_BASE_NAME
        ).build()
    }
}

const val DATA_BASE_NAME = "LeBonCoinTestV1"