package com.leboncoin.albumsample.presentation.di

import android.content.Context
import android.content.SharedPreferences
import com.leboncoin.albumsample.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideEnvironment(): String {
        return BuildConfig.ENV
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext appContext: Context
    ): SharedPreferences {
        return with(appContext) {
            getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        }
    }
}