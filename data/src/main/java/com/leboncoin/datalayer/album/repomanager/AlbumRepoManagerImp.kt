package com.leboncoin.datalayer.album.repomanager

import android.content.SharedPreferences
import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.album.albumsRepo.AlbumLocalRepository
import com.leboncoin.domain.album.albumsRepo.AlbumRemoteRepository
import com.leboncoin.domain.album.albumsRepo.AlbumRepoManager
import com.leboncoin.domain.album.albumsRepo.ConnectivityManager
import com.leboncoin.domain.tools.networkBoundResource
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class AlbumRepoManagerImp @Inject constructor(
    private val albumRemoteRepository: AlbumRemoteRepository,
    private val localAlbumRepository: AlbumLocalRepository,
    private val connectivityManager: ConnectivityManager,
    private var sharedPreferences: SharedPreferences
) : AlbumRepoManager {

    override suspend operator fun invoke(
        isForceRefresh: Boolean
    ) =
        networkBoundResource(
            isDbEmpty = {
                localAlbumRepository.isAlbumEmpty()
            },
            query = {
                localAlbumRepository.getAlbums()
            },

            saveFetchResult = { albums ->
                localAlbumRepository.saveAlbums(albums)
            },

            fetch = {
                //save timeStamp of the last Api call
                sharedPreferences.edit()
                    .putString(TIMESTAMP_LAST_ALBUM_API, LocalDateTime.now().toString()).apply()
                albumRemoteRepository.getAlbums()
            },
            shouldFetch = {
                // if "isForcePush" ,bypass all conditions and fetch from remote , otherwise ;
                // if db is empty or it has been $MINUTES_OFFSET minutes since the last api call then
                // it should fetch from remote
                val lastTimeStamp = sharedPreferences.getString(TIMESTAMP_LAST_ALBUM_API, null)

                if (isForceRefresh)
                    true
                else if (lastTimeStamp != null) {
                    val differenceInMinutes = ChronoUnit.MINUTES.between(
                        LocalDateTime.parse(lastTimeStamp),
                        LocalDateTime.now()
                    )
                    localAlbumRepository.isAlbumEmpty() || differenceInMinutes >= MINUTES_OFFSET
                } else
                    localAlbumRepository.isAlbumEmpty()
            },

            networkFLowObserver = {
                connectivityManager.observeConnectivity()
            },

            dbUpdateStrategy = { remoteAlbums, localAlbums ->
                if (localAlbums.isNotEmpty())
                    updateRemoteWithLocalFavorites(remoteAlbums, localAlbums)
                else
                    remoteAlbums
            }
        )


    //Update strategy ; update everything except the favorite flags of albums
    private fun updateRemoteWithLocalFavorites(
        remoteAlbums: List<DomainAlbum>,
        localAlbums: List<DomainAlbum>
    ): ArrayList<DomainAlbum> {
        val localFavorites = localAlbums.filter { it.isFavorite }

        val remoteUpdatedWithLocalFavorites = remoteAlbums
            .filter { it.albumId in localFavorites.map { it.albumId } }
            .map { remote ->
                remote.copy(isFavorite = localAlbums.first { it.albumId == remote.albumId }.isFavorite)
            }
        val remoteWithoutLocalFavoritesList = remoteAlbums
            .filter { it.albumId !in remoteUpdatedWithLocalFavorites.map { it.albumId } } as ArrayList<DomainAlbum>

        return (remoteWithoutLocalFavoritesList + remoteUpdatedWithLocalFavorites) as ArrayList<DomainAlbum>

    }
}

const val MINUTES_OFFSET = 5 // fetchData From api every 5 minutes
const val TIMESTAMP_LAST_ALBUM_API = "TIMESTAMP_LAST_ALBUM_API"