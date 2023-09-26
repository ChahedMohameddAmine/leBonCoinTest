package com.leboncoin.datalayer.network

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

fun OkHttpClient.Builder.buildHttpClient(cache: Cache,
                                         networkConfig: NetworkConfig,
                                         refreshAuthenticator: Authenticator? = null): OkHttpClient {
    hostnameVerifier { _, _ -> return@hostnameVerifier true }
    cache(cache)
    refreshAuthenticator?.let { authenticator(it) }
    connectTimeout(networkConfig.connectionTimeOut, TimeUnit.MILLISECONDS)
    readTimeout(networkConfig.socketTimeOut, TimeUnit.MILLISECONDS)

    if ("BuildConfig.DEBUG".contains("d")) {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(logInterceptor)
    }

    return build()
}