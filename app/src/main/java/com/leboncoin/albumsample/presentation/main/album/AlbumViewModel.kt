package com.leboncoin.albumsample.presentation.main.album

import androidx.lifecycle.viewModelScope
import com.leboncoin.albumsample.presentation.main.album.mapping.toAlbumUi
import com.leboncoin.albumsample.presentation.main.album.state_event.AlbumEvent
import com.leboncoin.albumsample.presentation.main.album.state_event.AlbumUiState
import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.album.getAlbums.GetAlbumsUseCaseImp
import com.leboncoin.domain.album.updateAlbum.UpdateAlbumUseCase
import com.leboncoin.domain.tools.oneTimeEvent.OneTimeNetworkState
import com.leboncoin.domain.tools.oneTimeEvent.setOneTimeErrorNetwork
import com.leboncoin.domain.tools.oneTimeEvent.setOneTimeNetworkRestored
import com.leboncoin.domain.tools.oneTimeEvent.setOneTimeNoDataErrorNetwork
import com.leboncoin.domain.tools.oneTimeEvent.setOneTimeNoDataErrorServer
import com.leboncoin.domain.tools.oneTimeEvent.setOneTimeServerError
import com.leboncoin.domain.tools.state.GCResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCaseImp,
    updateAlbumUseCase: UpdateAlbumUseCase
) : BaseAlbumViewModel(updateAlbumUseCase) {

    private var networkState: OneTimeNetworkState? = OneTimeNetworkState(OneTimeNetworkState.NONE)

    override fun reduceState(
        currentState: AlbumUiState, event: AlbumEvent
    ): AlbumUiState = when (event) {
        is AlbumEvent.ScreenStarted -> {
            getAlbums()
            currentState.copy(isLoading = true)
        }

        is AlbumEvent.OnRefreshAlbum -> {
            getAlbums(isForceRefresh = true)
            currentState.copy(isLoading = true)
        }

        is AlbumEvent.AlbumsFetched -> {
            event.uiState
        }

        is AlbumEvent.AlbumsFetcheds -> {

            when (event.uiState) {
                is GCResult.Success -> {
                    if (currentState.data.isEmpty())
                        handleData(event.uiState.data)

                    currentState.copy(
                        isLoading = false,
                    )
                }

                is GCResult.Error -> {
                    if (currentState.data.isEmpty() && event.uiState.data != null)
                        handleData(event.uiState.data!!)

                    currentState.copy(
                        isLoading = false,
                        networkState = processError(event.uiState)
                    )
                }

                is GCResult.ErrorNetwork -> {
                    if (currentState.data.isEmpty() && event.uiState.data != null)
                        handleData(event.uiState.data!!)

                    currentState.copy(
                        isLoading = false,
                        networkState = processError(event.uiState)

                    )
                }

                GCResult.Loading -> {
                    currentState.copy(
                        isLoading = true
                    )
                }

                is GCResult.NetworkRestored -> {
                    if (currentState.data.isEmpty())
                        handleData(event.uiState.data)

                    currentState.copy(
                        isLoading = false,
                        networkState = networkState.setOneTimeNetworkRestored()
                    )
                }
            }
        }

    }


    private fun getAlbums(isForceRefresh: Boolean = false) {
        viewModelScope.launch {
            getAlbumsUseCase.invoke(
                isForceRefresh
            )
                .flowOn(albumApiExceptionHandler + Dispatchers.IO)
                .collectLatest { result ->
                    launch { executePendingFavorite() }
                    launch { state.handleEvent(AlbumEvent.AlbumsFetcheds(result)) }
                }
        }
    }


    private fun processError(
        result: GCResult<List<DomainAlbum>>? = null
    ): OneTimeNetworkState? = when (result) {
        is GCResult.Error ->
            if (result.data == null)
                networkState.setOneTimeNoDataErrorServer()
            else
                networkState.setOneTimeServerError()

        is GCResult.ErrorNetwork ->
            if (result.data == null)
                networkState.setOneTimeNoDataErrorNetwork()
            else
                networkState.setOneTimeErrorNetwork()

        else -> null
    }

    private fun handleData(
        albumsFlow: Flow<List<DomainAlbum>>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            albumsFlow.flowOn(albumApiExceptionHandler + Dispatchers.IO)
                .collectLatest {
                    state.handleEvent(
                        AlbumEvent.AlbumsFetched(
                            state.value.copy(
                                data = it.map { it.toAlbumUi() },
                                isLoading = false,
                            )
                        )
                    )
                }
        }
    }


    private suspend fun executePendingFavorite() = mutex.withLock {
        viewModelScope.takeIf { pendingFavoriteAlbums.isNotEmpty() }?.launch(Dispatchers.Default) {
            pendingFavoriteAlbums.forEach { setAsFavoriteMutex(it) }
            pendingFavoriteAlbums.clear()
        }

    }


}
