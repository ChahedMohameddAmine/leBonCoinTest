package com.leboncoin.domain.album.albumsRepo

import com.leboncoin.domain.album.DomainAlbum
import kotlinx.coroutines.flow.Flow

interface AlbumLocalRepository {
    suspend fun saveAlbums(domainAlbums: List<DomainAlbum>)
    fun isAlbumEmpty() : Boolean
    suspend fun getAlbums(): Flow<List<DomainAlbum>>
    suspend fun updateAlbum(album : DomainAlbum)
}