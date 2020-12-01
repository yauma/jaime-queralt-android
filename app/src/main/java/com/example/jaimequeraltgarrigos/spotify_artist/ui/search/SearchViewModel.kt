package com.example.jaimequeraltgarrigos.spotify_artist.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.jaimequeraltgarrigos.spotify_artist.repository.ArtistError
import com.example.jaimequeraltgarrigos.spotify_artist.repository.ArtistRepositoryImpl
import com.example.jaimequeraltgarrigos.spotify_artist.utils.asNetworkException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val repositoryImpl: ArtistRepositoryImpl,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val artists = repositoryImpl.artists

    private val _snackBar = MutableLiveData<String?>()

    val snackbar: LiveData<String?>
        get() = _snackBar

    private val _spinner = MutableLiveData<Boolean>(false)

    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    fun queryMade(query: String) {
        launchDataLoad {
            fetchArtist(query)
        }
    }

    private suspend fun fetchArtist(query: String) {
        repositoryImpl.fetchArtist(query)
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: ArtistError) {
                _snackBar.value = error.message + error.cause?.let { asNetworkException(it) }
            } finally {
                _spinner.value = false
            }
        }
    }

    fun clearAdapter() {
        launchDataLoad {
            cleanCache()
        }
    }

    private suspend fun cleanCache() {
        repositoryImpl.cleanCache()
    }
}