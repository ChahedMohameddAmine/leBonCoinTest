package com.leboncoin.datalayer.network

import com.leboncoin.datalayer.network.NetworkConfig.Companion.DEV
import com.leboncoin.datalayer.network.NetworkConfig.Companion.PREPROD
import com.leboncoin.datalayer.network.NetworkConfig.Companion.PROD
import com.leboncoin.datalayer.network.NetworkConfig.Companion.QUALIF

class NetworkConfig private constructor(val baseUrl: String,
                                        val connectionTimeOut: Long = 30 * SECOND,
                                        val socketTimeOut: Long = connectionTimeOut) {

    companion object {
        val PROD = NetworkConfig(baseUrl = "https://static.leboncoin.fr")

        val PREPROD = NetworkConfig(baseUrl = "https://static.leboncoin.fr",
                                    connectionTimeOut = 25 * SECOND
        )

        val QUALIF = NetworkConfig(baseUrl = "https://static.leboncoin.fr",
                                   connectionTimeOut = 25 * SECOND
        )

        val DEV = NetworkConfig(baseUrl = "https://static.leboncoin.fr",
                                connectionTimeOut =  180 * SECOND,
                                socketTimeOut = 180 * SECOND
        )

        const val CACHE_SIZE = 10 * MEGA
    }

}

private const val SECOND = 1000L
private const val MEGA = 1024 * 1024L

typealias Environment = String

fun Environment.toNetworkConfig() = when {
    contentEquals("PROD") -> PROD
    contentEquals("PREPROD") -> PREPROD
    contentEquals("QUALIF") -> QUALIF
    contentEquals("DEV") -> DEV
    else -> QUALIF
}

