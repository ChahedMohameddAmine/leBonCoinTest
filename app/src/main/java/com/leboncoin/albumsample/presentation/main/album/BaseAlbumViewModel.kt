package com.leboncoin.albumsample.presentation.main.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leboncoin.albumsample.presentation.main.album.mapping.AlbumUi
import com.leboncoin.albumsample.presentation.main.album.mapping.toAlbumUi
import com.leboncoin.albumsample.presentation.main.album.state_event.AlbumEvent
import com.leboncoin.albumsample.presentation.main.album.state_event.AlbumUiState
import com.leboncoin.albumsample.presentation.tools.StateReducerFlow
import com.leboncoin.domain.album.updateAlbum.UpdateAlbumUseCase
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState.Companion.IS_ERROR_SERVER
import com.leboncoin.domain.tools.oneTimeEvent.toOneTimeNetworkState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BaseAlbumViewModel constructor(
    private val updateAlbumUseCase: UpdateAlbumUseCase
) : ViewModel() {

    protected val mutex = Mutex()
    protected var pendingFavoriteAlbums: ArrayList<AlbumUi> = arrayListOf()


    val state = StateReducerFlow(
        initialState = AlbumUiState.initial, reduceState = ::reduceState
    )

    protected abstract fun reduceState(
        currentState: AlbumUiState, event: AlbumEvent
    ): AlbumUiState


    open fun setAsFavorite(albumUi: AlbumUi) {
        viewModelScope.launch(Dispatchers.Default) {
            if (state.value.isLoading)
                mutex.withLock { pendingFavoriteAlbums += albumUi }
            else
                updateAlbumUseCase.invoke(album = albumUi.toAlbumUi())
        }
    }

    open suspend fun setAsFavoriteMutex(albumUi: AlbumUi) = mutex.withLock {
        if (state.value.isLoading)
            mutex.withLock { pendingFavoriteAlbums += albumUi }
        else
            updateAlbumUseCase.invoke(album = albumUi.toAlbumUi())
    }


    protected open val albumApiExceptionHandler = CoroutineExceptionHandler { _, exception ->
        state.handleEvent(
            AlbumEvent.AlbumsFetched(
                state.value.copy(
                    isLoading = false,
                    networkState = IS_ERROR_SERVER.toOneTimeNetworkState()
                )
            )
        )
    }


}
