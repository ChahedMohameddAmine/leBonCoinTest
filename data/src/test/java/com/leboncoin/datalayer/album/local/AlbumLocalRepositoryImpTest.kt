package com.leboncoin.datalayer.album.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.leboncoin.datalayer.album.mapping.AlbumMapper
import com.leboncoin.domain.album.DomainAlbum
import com.leboncoin.domain.album.albumsRepo.AlbumLocalRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AlbumLocalRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()

    // Mock dependencies
    @Mock
    private lateinit var albumDao: AlbumDao

    @Mock
    private lateinit var albumMapper: AlbumMapper

    // Class under test
    private lateinit var repository: AlbumLocalRepository


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = AlbumLocalRepositoryImp(albumDao, albumMapper)

        // Set the main dispatcher for testing
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun testSaveAlbums(): Unit = runBlocking {
        // Given a list of domain albums
        val domainAlbums = listOf(
            DomainAlbum(
                albumId = 0,
                id = 0,
                title = "title",
                url = "url",
                thumbnailUrl = "thumbnailUrl",
                isFavorite = false,
            )
        )

        // Mock the mapping from domain to repo album
        `when`(albumMapper.domainToRepoAlbum(domainAlbums[0])).thenReturn(
            AlbumEntity(
                albumId = domainAlbums.first().albumId,
                id = domainAlbums.first().id,
                title = domainAlbums.first().title,
                url = domainAlbums.first().url,
                thumbnailUrl = domainAlbums.first().thumbnailUrl,
                isFavorite = domainAlbums.first().isFavorite,
            )
        )

        // When saveAlbums is called
        repository.saveAlbums(domainAlbums)

        // Then verify that insertList was called on albumDao with the mapped repo albums
        verify(albumDao).insertList(ArgumentMatchers.any()) // You can use Hamcrest or other matchers
    }







}