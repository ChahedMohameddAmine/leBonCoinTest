package com.leboncoin.datalayer.album.mapping

import com.leboncoin.datalayer.album.local.AlbumEntity
import com.leboncoin.datalayer.album.remote.retrofit.AlbumResponse
import com.leboncoin.domain.album.DomainAlbum
import javax.inject.Inject


class AlbumMapperImpl @Inject constructor() : AlbumMapper {
    override fun domainToRepoAlbum(input: DomainAlbum): AlbumEntity =
        input.run {
            AlbumEntity(
                albumId = albumId,
                id = id,
                title = title,
                url = url,
                thumbnailUrl = thumbnailUrl,
                isFavorite = isFavorite
            )
        }


    override fun remoteToDomainAlbum(input: AlbumResponse): DomainAlbum =
        input.run {
            DomainAlbum(
                albumId = albumId,
                id = id,
                title = title,
                url = url,
                thumbnailUrl = thumbnailUrl,
                isFavorite = false
            )
        }


    override fun remoteToRepoAlbum(input: AlbumResponse): AlbumEntity =
        input.run {
            AlbumEntity(
                albumId = albumId,
                id = id,
                title = title,
                url = url,
                thumbnailUrl = thumbnailUrl,
                isFavorite = false
            )
        }

    override fun repoToDomainAlbum(input: AlbumEntity): DomainAlbum =
        input.run {
            DomainAlbum(
                albumId = albumId,
                id = id,
                title = title,
                url = url,
                thumbnailUrl = thumbnailUrl,
                isFavorite = isFavorite
            )
        }

}