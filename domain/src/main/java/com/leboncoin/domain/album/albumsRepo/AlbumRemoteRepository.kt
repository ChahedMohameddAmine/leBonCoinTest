package com.leboncoin.domain.album.albumsRepo

import com.leboncoin.domain.album.DomainAlbum

interface AlbumRemoteRepository {
    suspend fun getAlbums(): List<DomainAlbum>
}