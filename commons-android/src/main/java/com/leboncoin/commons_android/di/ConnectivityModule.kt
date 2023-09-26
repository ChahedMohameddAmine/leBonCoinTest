package com.leboncoin.commons_android.di


import com.leboncoin.commons_android.connectivity.ConnectivityManagerImp
import com.leboncoin.domain.album.albumsRepo.ConnectivityManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    abstract fun bindConnectivityManagerImp(connectivityManager: ConnectivityManagerImp): com.leboncoin.domain.album.albumsRepo.ConnectivityManager

}