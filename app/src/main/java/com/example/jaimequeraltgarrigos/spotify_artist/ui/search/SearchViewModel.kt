package com.example.jaimequeraltgarrigos.spotify_artist.ui.search

import android.app.DownloadManager
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaimequeraltgarrigos.spotify_artist.repository.ArtistRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel @ViewModelInject constructor(
    private val repositoryImpl: ArtistRepositoryImpl,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val artists = repositoryImpl.artists

    fun artistSearch(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                fetchArtist(query)
            }
        }
    }

    suspend fun fetchArtist(query: String){
        repositoryImpl.fetchArtist(query)
    }
}