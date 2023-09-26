package com.leboncoin.albumsample.presentation.main.album.mapping

data class AlbumUi(
    var albumId: Int,
    var id: Int,
    var title: String,
    var url: String,
    var thumbnailUrl: String,
    var isFavorite: Boolean
)