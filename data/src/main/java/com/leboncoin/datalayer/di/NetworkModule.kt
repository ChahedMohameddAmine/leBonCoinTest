package com.leboncoin.datalayer.di

import android.content.Context
import com.google.gson.Gson
import com.leboncoin.datalayer.album.remote.retrofit.AlbumApi
import com.leboncoin.datalayer.network.NetworkConfig
import com.leboncoin.datalayer.network.NetworkConfig.Companion.CACHE_SIZE
import com.leboncoin.datalayer.network.buildHttpClient
import com.leboncoin.datalayer.network.toNetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkConfig(environment : String): NetworkConfig {
        return environment.toNetworkConfig()
    }

    @Provides
    @Singleton
    fun provideAlbumApi(retrofit: Retrofit): AlbumApi {
        return retrofit.create(AlbumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, CACHE_SIZE)
    }

    @Provides
    @Singleton
    @Named("defaultHttpClient")
    fun provideDefaultHttpClient(
        cache: Cache,
        networkConfig: NetworkConfig
    ): OkHttpClient {
        return OkHttpClient.Builder().buildHttpClient(cache = cache, networkConfig = networkConfig)
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        cache: Cache,
        networkConfig: NetworkConfig
    ): OkHttpClient {
        return OkHttpClient.Builder().buildHttpClient(
            cache = cache,
            networkConfig = networkConfig
        )
    }

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideRetrofit(
        networkConfig: NetworkConfig,
        gson: Gson,
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(networkConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}