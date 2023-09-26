package com.leboncoin.domain.di


import com.leboncoin.domain.album.getAlbums.GetAlbumsUseCaseImp
import com.leboncoin.domain.album.getAlbums.GetAlbumsUseCase
import com.leboncoin.domain.album.updateAlbum.UpdateAlbumUseCase
import com.leboncoin.domain.album.updateAlbum.UpdateAlbumUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {


    @Binds
    abstract fun bindGetAlbumsUseCaseV2(getAlbumsUseCase: GetAlbumsUseCaseImp): GetAlbumsUseCase

    @Binds
    abstract fun bindUpdateAlbumUseCase(updateAlbumUseCaseImp: UpdateAlbumUseCaseImp): UpdateAlbumUseCase

}
