package com.leboncoin.domain.tools.state

import kotlinx.coroutines.flow.Flow


sealed interface GCResult<out S > {


    data class Success<S>(
        val data: Flow<S>
    ) : GCResult<S>



    data class NetworkRestored<S>(val data: Flow<S>) : GCResult<S>
    data class Error<S>(val exception: Throwable? = null,val data: Flow<S>? = null ) : GCResult<S>
    object Loading : GCResult<Nothing>
    data class ErrorNetwork<S>(val data: Flow<S>? = null) : GCResult<S>
}




