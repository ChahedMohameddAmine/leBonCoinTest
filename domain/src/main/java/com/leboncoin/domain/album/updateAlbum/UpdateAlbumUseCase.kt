package com.leboncoin.domain.album.updateAlbum

import com.leboncoin.domain.album.DomainAlbum

interface UpdateAlbumUseCase {
    suspend operator fun invoke(album : DomainAlbum)
}