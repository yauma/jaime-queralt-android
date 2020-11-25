package com.example.jaimequeraltgarrigos.spotify_artist.repository

import android.app.Application
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDao
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.Album
import kotlinx.coroutines.withTimeout

class ArtistRepositoryImpl(
    private val artistDao: ArtistDao,
    private val network: MainNetwork
) :
    ArtistRepository {

    val artists = artistDao.getArtistWithAlbum()

    override suspend fun fetchArtist(query: String) {
        val artistIds = mutableListOf<String>()
        try {
            val artists = withTimeout(5000) {
                network.fetchArtists(query)
            }
            val alb: List<Album> = artists.artists.items.flatMap {
                network.getAlbums(it.id).items
            }
            val i = alb
            print(i)
        } catch (error: Throwable) {
            throw ArtistRefreshError("Unable to fetch Artists", error)
        }
    }
}


class ArtistRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)
