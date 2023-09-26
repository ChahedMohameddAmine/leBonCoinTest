package com.leboncoin.datalayer.album.remote

import com.leboncoin.datalayer.album.mapping.AlbumMapper
import com.leboncoin.datalayer.album.remote.retrofit.AlbumApi
import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.album.albumsRepo.AlbumRemoteRepository
import javax.inject.Inject

class AlbumRemoteRepositoryImp @Inject constructor(
    private val api: AlbumApi,
    private val albumMapper: AlbumMapper
) : AlbumRemoteRepository {

    override suspend fun getAlbums() :List<DomainAlbum> =
        api.getAlbums().map {
            albumMapper.remoteToDomainAlbum(it)
        }
}