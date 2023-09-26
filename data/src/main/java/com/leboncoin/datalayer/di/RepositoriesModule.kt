package com.leboncoin.datalayer.di

import com.leboncoin.datalayer.album.local.AlbumLocalRepositoryImp
import com.leboncoin.datalayer.album.remote.AlbumRemoteRepositoryImp
import com.leboncoin.datalayer.album.repomanager.AlbumRepoManagerImp
import com.leboncoin.domain.album.albumsRepo.AlbumLocalRepository
import com.leboncoin.domain.album.albumsRepo.AlbumRemoteRepository
import com.leboncoin.domain.album.albumsRepo.AlbumRepoManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindAlbumRepoManager(albumRepoManagerImp: AlbumRepoManagerImp): AlbumRepoManager

    @Binds
    abstract fun bindAlbumLocalRepository(albumLocalRepositoryImp: AlbumLocalRepositoryImp): AlbumLocalRepository

    @Binds
    abstract fun bindAlbumRemoteRepository(albumRemoteRepositoryImp: AlbumRemoteRepositoryImp): AlbumRemoteRepository
}