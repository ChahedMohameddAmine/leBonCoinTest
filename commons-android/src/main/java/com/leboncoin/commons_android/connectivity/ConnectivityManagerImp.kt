package com.leboncoin.commons_android.connectivity

import android.content.Context
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import com.leboncoin.domain.album.albumsRepo.ConnectivityManager
import com.leboncoin.domain.tools.state.ConnectionState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import java.net.URL
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class ConnectivityManagerImp @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityManager {

    private lateinit var lastConnectionState: ConnectionState
    private var isFirstSwitchNetwork =true


    override fun isNetworkAvailable(): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager?
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                if (activeNetwork == null) return@run false  // return false if network is null
                val actNetwork = getNetworkCapabilities(activeNetwork)
                    ?: return@run false // return false if Network Capabilities is null

                val isWifiOrCellular =
                    actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNetwork.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    )

                val isConnectedToInternet =
                    (actNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            && actNetwork.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    ))

                return@run (isWifiOrCellular && isConnectedToInternet && checkInternetConnection())
            } ?: false
        } else {
            try {
                if (cm?.activeNetworkInfo == null)
                    false
                else
                    checkInternetConnection() && cm.activeNetworkInfo?.isConnected!!
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun observeConnectivity(): Flow<ConnectionState> =
        callbackFlow {

            lastConnectionState =
                if (isNetworkAvailable()) ConnectionState.Available else ConnectionState.Unavailable

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager

            val callback = networkCallback { isConnected ->
                //ensureActive()
                var connectionState = isConnected

                if (connectionState is ConnectionState.Available &&
                    lastConnectionState != connectionState
                )
                    connectionState = ConnectionState.Restored


                //send ConnectionState Event
                if(lastConnectionState !=connectionState || isFirstSwitchNetwork)
                {
                    trySend(connectionState)
                    isFirstSwitchNetwork = false
                }

                lastConnectionState = connectionState


            }

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

                .build()

            // Set current state

            //trySend(currentState)

           /* if (lastConnectionState !is ConnectionState.Unavailable) {
                trySend(lastConnectionState)
                //channel.close()
            }*/

            connectivityManager.registerNetworkCallback(networkRequest, callback)

            /*if (!channel.isClosedForSend)
                trySend(lastConnectionState)*/

            trySend(lastConnectionState)


            // Remove callback when not used
            awaitClose {
                // Remove listeners
                kotlin.runCatching { connectivityManager.unregisterNetworkCallback(callback) }

            }


        }


    private fun networkCallback(callback: (ConnectionState) -> Unit): android.net.ConnectivityManager.NetworkCallback {
        return object : android.net.ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                callback.invoke(ConnectionState.Available)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                callback.invoke(ConnectionState.Unavailable)
            }
        }
    }

    @Synchronized
    private fun checkInternetConnection(): Boolean {
        return try {
            val urlConnection =
                URL("https://clients3.google.com/generate_204")
                    .openConnection() as HttpsURLConnection
            urlConnection.setRequestProperty("User-Agent", "Android")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1000
            urlConnection.connect()
            val isConnected =
                urlConnection.responseCode == 204 && urlConnection.contentLength == 0
            (isConnected)
        } catch (e: Exception) {
            (false)
        }

    }
}