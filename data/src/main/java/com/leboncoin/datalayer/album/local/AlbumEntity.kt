package com.leboncoin.datalayer.album.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlbumEntity(
    @PrimaryKey var albumId: Int,
    var id: Int,
    var title: String,
    var url: String,
    var thumbnailUrl: String,
    var isFavorite: Boolean
)