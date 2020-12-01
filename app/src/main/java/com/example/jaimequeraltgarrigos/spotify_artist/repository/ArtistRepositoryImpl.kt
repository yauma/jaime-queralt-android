package com.example.jaimequeraltgarrigos.spotify_artist.repository

import com.example.jaimequeraltgarrigos.spotify_artist.data.database.*
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.AlbumWithSongsAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.ArtistsWithAlbumAndSongsAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs.SongAPI
import com.example.jaimequeraltgarrigos.spotify_artist.di.DefaultDispatcher
import com.example.jaimequeraltgarrigos.spotify_artist.di.IoDispatcher
import com.example.jaimequeraltgarrigos.spotify_artist.utils.Mapper
import kotlinx.coroutines.*

class ArtistRepositoryImpl(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val artistDB: ArtistDataBase,
    private val network: MainNetwork
) :
    ArtistRepository {
    private val artistDao = artistDB.artistDao
    val artists = artistDao.getArtistsWithAlbumAndSongs()

    override suspend fun fetchArtist(query: String) {
        withContext(ioDispatcher) {
            try {
                cleanCache()
                val response: List<ArtistsWithAlbumAndSongsAPI> = withTimeout(5000) {
                    network.fetchArtists(query)
                }.artists.items.map { artist ->
                    val albums = network.getAlbums(artist.id).items.map {
                        AlbumWithSongsAPI(it, network.getAlbumSongs(it.id).items)
                    }
                    ArtistsWithAlbumAndSongsAPI(artist, albums)
                }

                response.forEach {
                    println(it.toString())
                }

                /*val artists: List<Artist> = withTimeout(5000) {
                    network.fetchArtists(query)
                }.artists.items.map {
                    Mapper.artistAPIToDBEntity(it)
                }
                //artistDao.insertArtists(Mapper.artistsAPIListToDBEntityList(artistsSearchResponse.artists.items))

                val albums = artists.flatMap {
                    network.getAlbums(it.artistId).items
                }.distinctBy {
                    it.name
                }.map {
                    Mapper.albumAPIToDBEntity(it)
                }
                //artistDao.insertAlbums(Mapper.albumAPIListToDBEntityList(albumsApi))

                val listAlbumSongs: List<List<Song>> = albums.map { album ->
                    async {
                        network.getAlbumSongs(album.albumId).items
                    }
                }.map {
                    it.await()
                }.map {
                    Mapper.songsAPIListToDBEntityList(it)
                }*/

            } catch (error: Throwable) {
                throw ArtistError("Unable to fetch Artists ", error)
            }
        }
    }

    suspend fun cleanCache() {
        artistDB.clearAllTables()
    }
}

/*suspend fun fetchItems(itemIds: List<AlbumAPI>): Map<AlbumAPI, List<SongAPI>> {
    val itemById = mutableMapOf<Long, Item>()
    coroutineScope {
        itemIds.forEach { itemId ->
            launch { itemById[itemId] = MyService.getItem(itemId) }
        }
    }
    return itemById
}*/

class ArtistError(message: String, cause: Throwable?) : Throwable(message, cause)