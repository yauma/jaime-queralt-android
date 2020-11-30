package com.example.jaimequeraltgarrigos.spotify_artist.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.jaimequeraltgarrigos.spotify_artist.MainCoroutineScopeRule
import com.example.jaimequeraltgarrigos.spotify_artist.captureValues
import com.example.jaimequeraltgarrigos.spotify_artist.data.FakeData
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDataBase
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistWithAlbums
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.getOrAwaitValue
import com.example.jaimequeraltgarrigos.spotify_artist.repository.ArtistRepositoryImpl
import com.example.jaimequeraltgarrigos.spotify_artist.ui.viewmodel.SearchViewModel
import junit.framework.TestCase
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.HttpException
import retrofit2.Response

class SearchViewModelTest {
    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    // Subject under test
    private lateinit var viewModel: SearchViewModel

    private val artistDataBase = Mockito.mock(ArtistDataBase::class.java)

    private lateinit var artistDao: FakeData.FakeDao

    private lateinit var network: MainNetwork

    private lateinit var repositoryImpl: ArtistRepositoryImpl

    private val handle: SavedStateHandle = SavedStateHandle()

    @Before
    fun setupViewModel() {
        network = FakeData.FakeNetwork(FakeData.FAKE_ARTISTS, FakeData.FAKE_ALBUMS)
        artistDao = FakeData.FakeDao()
        Mockito.`when`(artistDataBase.artistDao).thenReturn(artistDao)
        repositoryImpl =
            ArtistRepositoryImpl(
                testDispatcher,
                artistDataBase, network
            )
        viewModel =
            SearchViewModel(
                repositoryImpl,
                handle
            )
    }

    @Test
    fun spinner_value_should_be_false_at_the_beginning() {
        //When
        val value = viewModel.spinner.getOrAwaitValue()

        //Then
        assertThat(value, CoreMatchers.`is`(false))
    }

    @Test
    fun whenOnSnackbarShownCalled_thenValueShouldBeNull() {
        //When
        viewModel.onSnackbarShown()

        //Then
        val value = viewModel.snackbar.getOrAwaitValue()
        TestCase.assertEquals(value, null)
    }

    @Test
    fun whenQueryMadeThenSpinnerShownAndHide() {
        coroutineScope.runBlockingTest {
            viewModel.spinner.captureValues {
                TestCase.assertEquals(values, listOf(false))
                viewModel.queryMade("")
                TestCase.assertEquals(values, listOf(false, true, false))
                TestCase.assertEquals(
                    viewModel.artists.getOrAwaitValue(),
                    FakeData.FakeArtistWithAlbumList
                )
            }
        }
    }

    @Test
    fun whenCleanThenArtistsMustBeEmpty() {
        coroutineScope.runBlockingTest {
            viewModel.spinner.captureValues {
                TestCase.assertEquals(values, listOf(false))
                viewModel.queryMade("")
                TestCase.assertEquals(values, listOf(false, true, false))
                TestCase.assertEquals(
                    viewModel.artists.getOrAwaitValue(),
                    FakeData.FakeArtistWithAlbumList
                )
            }
            viewModel.clearAdapter()
            artistDao.clearCache()
            TestCase.assertEquals(
                viewModel.artists.getOrAwaitValue(),
                emptyList<ArtistWithAlbums>()
            )
        }
    }

    @Test
    fun whenQueryMadeThenDBisUpdated() {
        coroutineScope.runBlockingTest {
            viewModel.spinner.captureValues {
                viewModel.queryMade("")
                TestCase.assertEquals(
                    viewModel.artists.getOrAwaitValue(),
                    FakeData.FakeArtistWithAlbumList
                )
            }
        }
    }

    @Test
    fun whenError_itShowsErrorAndHidesSpinner() {
        coroutineScope.runBlockingTest {
            val networkCompletableFake = FakeData.MainNetworkCompletableFake()
            Mockito.`when`(artistDataBase.artistDao).thenReturn(artistDao)
            val repositoryImpl =
                ArtistRepositoryImpl(testDispatcher, artistDataBase, networkCompletableFake)
            val viewModel =
                SearchViewModel(
                    repositoryImpl,
                    handle
                )
            viewModel.spinner.captureValues {
                viewModel.queryMade("")
                TestCase.assertEquals(values, listOf(false, true))
                networkCompletableFake.sendErrorToArtistsCurrentRequests(makeErrorResult("Error"))
                TestCase.assertEquals(values, listOf(false, true, false))
            }
        }
    }

    @Test
    fun whenError_itShowsErrorText() = coroutineScope.runBlockingTest {
        val networkCompletableFake = FakeData.MainNetworkCompletableFake()
        Mockito.`when`(artistDataBase.artistDao).thenReturn(artistDao)
        val repositoryImpl =
            ArtistRepositoryImpl(testDispatcher, artistDataBase, networkCompletableFake)
        val viewModel =
            SearchViewModel(
                repositoryImpl,
                handle
            )
        viewModel.queryMade("")
        networkCompletableFake.sendErrorToArtistsCurrentRequests(makeErrorResult("An error"))
        assertEquals(
            viewModel.snackbar.getOrAwaitValue(),
            "Unable to fetch Artists Something went wrong..."
        )
        viewModel.onSnackbarShown()
        assertNull(viewModel.snackbar.getOrAwaitValue())
    }

    private fun makeErrorResult(result: String): HttpException {
        return HttpException(
            Response.error<String>(
                500,
                ResponseBody.create(
                    "applicatioFn/json".toMediaType(),
                    "\"$result\""
                )
            )
        )
    }
}