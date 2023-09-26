package com.leboncoin.domain.tools.state

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
    object Restored : ConnectionState()
}