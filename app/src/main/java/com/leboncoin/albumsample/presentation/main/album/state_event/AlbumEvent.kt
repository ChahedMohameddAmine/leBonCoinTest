package com.leboncoin.albumsample.presentation.main.album.state_event

import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.tools.state.GCResult


sealed class AlbumEvent {
    object ScreenStarted: AlbumEvent()
    object OnRefreshAlbum : AlbumEvent()
    data class AlbumsFetched(val uiState : AlbumUiState) : AlbumEvent()
    data class AlbumsFetcheds(val uiState : GCResult<List<DomainAlbum>>) : AlbumEvent()
}
