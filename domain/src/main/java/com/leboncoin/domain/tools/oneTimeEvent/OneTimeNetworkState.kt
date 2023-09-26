package com.leboncoin.domain.tools.oneTimeEvent

import androidx.annotation.IntDef


/**
 * Used to represent a one-shot UI event within an [MviViewState], so that we don't have to
 * toggle [Boolean] values or use timers in Rx or anything too wild. [consumeOnce] allows you to
 * process the event only once.
 *
 * Can store whatever data you might want - most of the time this would be a [String] or
 * res ID [Int].
 */
data class OneTimeNetworkState(@NetworkState val mPayload: Int? = null) :
    OneTimeEvent<Int>(mPayload) {


    @IntDef(
        NONE,
        IS_ERROR_NETWORK,
        IS_NETWORK_RESTORED,
        IS_ERROR_SERVER,
        IS_NO_DATA_ERROR_SERVER,
        IS_NO_DATA_ERROR_NETWORK
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NetworkState

    companion object {
        const val NONE = -1
        const val IS_ERROR_NETWORK = 0
        const val IS_NETWORK_RESTORED = 1
        const val IS_ERROR_SERVER = 2
        const val IS_NO_DATA_ERROR_SERVER = 3
        const val IS_NO_DATA_ERROR_NETWORK = 4
    }
}

fun Int.toOneTimeNetworkState() =
    OneTimeNetworkState(this)


fun OneTimeNetworkState?.setOneTimeServerError() = let {
    this?.resetEvent()
    OneTimeNetworkState.IS_ERROR_SERVER.toOneTimeNetworkState()
}

fun OneTimeNetworkState?.setOneTimeErrorNetwork() = let {
    this?.resetEvent()
    OneTimeNetworkState.IS_ERROR_NETWORK.toOneTimeNetworkState()
}

fun OneTimeNetworkState?.setOneTimeNetworkRestored() = let {
    this?.resetEvent()
    OneTimeNetworkState.IS_NETWORK_RESTORED.toOneTimeNetworkState()
}

fun OneTimeNetworkState?.setOneTimeNoDataErrorServer() = let {
    this?.resetEvent()
    OneTimeNetworkState.IS_NO_DATA_ERROR_SERVER.toOneTimeNetworkState()
}

fun OneTimeNetworkState?.setOneTimeNoDataErrorNetwork() = let {
    this?.resetEvent()
    OneTimeNetworkState.IS_NO_DATA_ERROR_NETWORK.toOneTimeNetworkState()
}
