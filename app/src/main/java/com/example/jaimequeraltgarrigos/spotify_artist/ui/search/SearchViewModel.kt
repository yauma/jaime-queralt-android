package com.example.jaimequeraltgarrigos.spotify_artist.ui.search

import android.app.DownloadManager
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaimequeraltgarrigos.spotify_artist.repository.ArtistRepositoryImpl
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val repositoryImpl: ArtistRepositoryImpl,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val artists = repositoryImpl.artists

    fun onMainViewCreated() {
        viewModelScope.launch {
            fetchArtist("Muse")
        }
    }

    private suspend fun fetchArtist(query: String) {
        repositoryImpl.fetchArtist(query)
    }
}