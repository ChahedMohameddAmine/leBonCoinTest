package com.leboncoin.albumsample.presentation.main.album.mapping

import com.leboncoin.domain.album.DomainAlbum


fun DomainAlbum.toAlbumUi() = AlbumUi(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
    isFavorite = isFavorite
)

fun AlbumUi.toAlbumUi() = DomainAlbum(
    albumId = albumId,
    id = id,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
    isFavorite = isFavorite
)

