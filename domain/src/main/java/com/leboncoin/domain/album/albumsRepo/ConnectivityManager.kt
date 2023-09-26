package com.leboncoin.domain.album.albumsRepo

import com.leboncoin.domain.tools.state.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ConnectivityManager {

    fun isNetworkAvailable() : Boolean

    suspend fun observeConnectivity() : Flow<ConnectionState>
}