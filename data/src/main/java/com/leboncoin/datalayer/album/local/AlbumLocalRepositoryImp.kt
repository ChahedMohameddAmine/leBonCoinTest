package com.leboncoin.datalayer.album.local

import com.leboncoin.datalayer.album.mapping.AlbumMapper
import com.leboncoin.domain.album.albumsRepo.AlbumLocalRepository
import com.leboncoin.domain.album.DomainAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumLocalRepositoryImp @Inject constructor(
    private val albumDao: AlbumDao,
    private val albumMapper: AlbumMapper,
) :
    AlbumLocalRepository {

    override suspend fun saveAlbums(albums: List<DomainAlbum>) {
        withContext(Dispatchers.IO) {
            albumDao.insertList(albums.map { albumMapper.domainToRepoAlbum(it) })
        }
    }

    override fun isAlbumEmpty() = albumDao.isEmpty()


    override suspend fun getAlbums() =
        withContext(Dispatchers.IO) {
            albumDao.getAlbums().map {
                it.map {
                    albumMapper.repoToDomainAlbum(it)
                }
            }
        }


    override suspend fun updateAlbum(album: DomainAlbum) {
        withContext(Dispatchers.IO) {
            albumDao.update(albumMapper.domainToRepoAlbum(album))
        }
    }


}