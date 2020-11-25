package com.example.jaimequeraltgarrigos.spotify_artist.repository

import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDao
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.utils.mapper.Mapper
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
            val artistsSearchResponse = withTimeout(5000) {
                network.fetchArtists(query)
            }
            artistDao.insertArtists(Mapper.artistsAPIListToDBEntityList(artistsSearchResponse.artists.items))
            val albumsApi: List<AlbumAPI> = artistsSearchResponse.artists.items.flatMap {
                network.getAlbums(it.id).items
            }
            artistDao.insertAlbums(Mapper.albumAPIListToDBEntityList(albumsApi))
        } catch (error: Throwable) {
            throw ArtistRefreshError("Unable to fetch Artists", error)
        }
    }
}


class ArtistRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)
