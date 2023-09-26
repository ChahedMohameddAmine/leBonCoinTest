package com.leboncoin.domain.album.getAlbums

import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.tools.state.GCResult
import kotlinx.coroutines.flow.Flow

interface GetAlbumsUseCase {
    suspend operator fun invoke(isForceRefresh: Boolean): Flow<GCResult<List<DomainAlbum>>>
}