package com.example.jaimequeraltgarrigos.spotify_artist.repository

import androidx.lifecycle.LiveData
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDao
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.withTimeout

class ArtistRepositoryImpl(private val artistDao: ArtistDao, private val network: MainNetwork) :
    ArtistRepository {

    val artists = artistDao.getArtistWithAlbum()

    override suspend fun fetchArtist(query: String) {
        try {
            val result = withTimeout(5000) {
                network.fetchArtists(query)
            }
            val i = result
        } catch (error: Throwable) {
            throw MoviesRefreshError("Unable to fetch Artists", error)
        }
    }
}


class MoviesRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)
