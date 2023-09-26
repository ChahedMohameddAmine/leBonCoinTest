package com.leboncoin.domain.album.updateAlbum

import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.album.albumsRepo.AlbumLocalRepository
import javax.inject.Inject

class UpdateAlbumUseCaseImp @Inject constructor(private val albumLocalRepository : AlbumLocalRepository) :
    UpdateAlbumUseCase {
    override suspend fun  invoke(album : DomainAlbum){
        albumLocalRepository.updateAlbum(album)
    }
}