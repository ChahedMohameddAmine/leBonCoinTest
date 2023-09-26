package com.leboncoin.domain.album.getAlbums

import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.album.albumsRepo.AlbumRepoManager
import com.leboncoin.domain.tools.state.GCResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCaseImp @Inject constructor(
    private val albumRepoManager: AlbumRepoManager,

    ) :
    GetAlbumsUseCase {
    override suspend fun invoke(isForceRefresh: Boolean): Flow<GCResult<List<DomainAlbum>>> =
        albumRepoManager.invoke(isForceRefresh )
}