package com.leboncoin.datalayer.di

import com.leboncoin.datalayer.album.mapping.AlbumMapper
import com.leboncoin.datalayer.album.mapping.AlbumMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DtoModule {

    @Binds
    abstract fun bindAlbumMapperImpl(albumMapperImpl : AlbumMapperImpl) : AlbumMapper
}