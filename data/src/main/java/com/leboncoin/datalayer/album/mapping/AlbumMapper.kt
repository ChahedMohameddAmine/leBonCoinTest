package com.leboncoin.datalayer.album.mapping

import com.leboncoin.datalayer.album.local.AlbumEntity
import com.leboncoin.datalayer.album.remote.retrofit.AlbumResponse
import com.leboncoin.domain.album.DomainAlbum


interface AlbumMapper {

    fun domainToRepoAlbum(input: DomainAlbum): AlbumEntity

    fun remoteToDomainAlbum(input: AlbumResponse): DomainAlbum

    fun remoteToRepoAlbum(input: AlbumResponse): AlbumEntity

    fun repoToDomainAlbum(input: AlbumEntity): DomainAlbum
}