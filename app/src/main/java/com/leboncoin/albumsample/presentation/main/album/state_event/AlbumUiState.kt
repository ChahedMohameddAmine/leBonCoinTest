package com.leboncoin.albumsample.presentation.main.album.state_event

import com.leboncoin.albumsample.presentation.main.album.mapping.AlbumUi
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeEvent
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState

data class AlbumUiState(
    val isLoading: Boolean,
    val networkState: OneTimeNetworkState?,
    val data: List<AlbumUi>
) {

    companion object {
        val initial = AlbumUiState(
            isLoading = true,
            networkState = null,
            data = emptyList()
        )
    }

}



