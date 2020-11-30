package com.example.jaimequeraltgarrigos.spotify_artist.repository

import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDataBase
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.di.IoDispatcher
import com.example.jaimequeraltgarrigos.spotify_artist.utils.Mapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class ArtistRepositoryImpl(
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val artistDB: ArtistDataBase,
    private val network: MainNetwork
) :
    ArtistRepository {
    private val artistDao = artistDB.artistDao
    val artists = artistDao.getArtistWithAlbum()

    override suspend fun fetchArtist(query: String) {
        withContext(defaultDispatcher) {
            try {
                cleanCache()
                val artistsSearchResponse = withTimeout(5000) {
                    network.fetchArtists(query)
                }
                artistDao.insertArtists(Mapper.artistsAPIListToDBEntityList(artistsSearchResponse.artists.items))
                val albumsApi: List<AlbumAPI> = artistsSearchResponse.artists.items.flatMap {
                    network.getAlbums(it.id).items
                }.distinctBy {
                    it.name
                }
                artistDao.insertAlbums(Mapper.albumAPIListToDBEntityList(albumsApi))
            } catch (error: Throwable) {
                throw ArtistError("Unable to fetch Artists ", error)
            }
        }
    }

    suspend fun cleanCache() {
        withContext(defaultDispatcher) {
            artistDB.clearAllTables()
        }
    }
}


class ArtistError(message: String, cause: Throwable?) : Throwable(message, cause)