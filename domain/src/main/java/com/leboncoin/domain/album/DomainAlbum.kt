package com.leboncoin.domain.album

data class DomainAlbum(
    var albumId: Int,
    var id: Int,
    var title: String,
    var url: String,
    var thumbnailUrl: String,
    var isFavorite: Boolean
)