package com.leboncoin.datalayer.repomanager

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertions.isTrue
import com.leboncoin.datalayer.album.local.AlbumDao
import com.leboncoin.datalayer.album.local.AlbumLocalRepositoryImp
import com.leboncoin.datalayer.album.mapping.AlbumMapper
import com.leboncoin.datalayer.album.remote.retrofit.AlbumApi
import com.leboncoin.domain.album.albumsRepo.AlbumLocalRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AlbumRepoManagerImpTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    private lateinit var albumRepoManager: AlbumRepoManager

    @Inject
    private lateinit var albumLocalRepository: AlbumLocalRepository

    @Inject
    private lateinit var albumDao: AlbumDao

    @Inject
    private lateinit var albumMapper: AlbumMapper

    private lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        hiltRule.inject()
        albumLocalRepository = AlbumLocalRepositoryImp(albumDao, albumMapper)
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Inject
    private lateinit var albumApi: AlbumApi


    @Test
    fun canGetAlbum() = runBlocking {
        // call the api
        val api = albumApi
        val responseAlbum = api.getAlbums()
        assertk.assertThat(responseAlbum.isNotEmpty()).isTrue()
    }

}