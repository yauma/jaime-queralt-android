package com.example.jaimequeraltgarrigos.spotify_artist.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.jaimequeraltgarrigos.spotify_artist.MainCoroutineScopeRule
import com.example.jaimequeraltgarrigos.spotify_artist.data.FakeData
import com.example.jaimequeraltgarrigos.spotify_artist.data.FakeData.FakeDao
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDao
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDataBase
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistWithAlbums
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ArtistRepositoryImplTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    val artistDataBase = mock(ArtistDataBase::class.java)
    private lateinit var artistDao: ArtistDao
    private lateinit var network: MainNetwork

    private lateinit var artists: LiveData<List<ArtistWithAlbums>>

    //class under test
    private lateinit var artistRepositoryImpl: ArtistRepositoryImpl

    @Before
    fun setup() {
        network = FakeData.FakeNetwork(FakeData.FAKE_ARTISTS, FakeData.FAKE_ALBUMS)
        artistDao = FakeDao()
        `when`(artistDataBase.artistDao).thenReturn(artistDao)
        artistRepositoryImpl = ArtistRepositoryImpl(testDispatcher, artistDataBase, network)
    }

    @Test
    fun getArtist_requestsAllArtistFromDataSource() = testDispatcher.runBlockingTest {
        //When
        artistRepositoryImpl.fetchArtist("")
        delay(1000L)
        //Then
        val value = artistRepositoryImpl.artists.getOrAwaitValue()
        TestCase.assertEquals(value, FakeData.FakeArtistWithAlbumList)
    }

    @Test(expected = ArtistError::class)
    fun whenRefreshTitleTimeout_throws() = testDispatcher.runBlockingTest {
        artistRepositoryImpl = ArtistRepositoryImpl(
            testDispatcher,
            artistDataBase,
            FakeData.FakeDataCompletableSource(FakeData.FAKE_ARTISTS, FakeData.FAKE_ALBUMS)
        )
        launch {
            artistRepositoryImpl.fetchArtist("")
        }
        advanceTimeBy(5_000)
    }

}