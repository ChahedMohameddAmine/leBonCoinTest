package com.leboncoin.datalayer.album.remote.retrofit

import retrofit2.http.GET

interface AlbumApi {
    @GET("/img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumResponse>
}