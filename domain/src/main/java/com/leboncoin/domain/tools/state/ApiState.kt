package com.leboncoin.domain.tools.state

sealed class ApiStateResponse {

    data class ServerError(
        val exception: Throwable
    ) : ApiStateResponse()

    data class ConnectionStateResponse(
        val connectionState: ConnectionState
    ) : ApiStateResponse()

    object Loading : ApiStateResponse()


}