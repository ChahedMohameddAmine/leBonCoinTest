package com.leboncoin.domain.album.albumsRepo

import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.tools.state.GCResult
import kotlinx.coroutines.flow.Flow

interface AlbumRepoManager {
    suspend operator fun invoke(isForceRefresh: Boolean): Flow<GCResult<List<DomainAlbum>>>
}