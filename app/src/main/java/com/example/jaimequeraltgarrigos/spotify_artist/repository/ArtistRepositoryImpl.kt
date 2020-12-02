package com.example.jaimequeraltgarrigos.spotify_artist.repository

import com.example.jaimequeraltgarrigos.spotify_artist.data.database.*
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs.SongAPI
import com.example.jaimequeraltgarrigos.spotify_artist.di.DefaultDispatcher
import com.example.jaimequeraltgarrigos.spotify_artist.di.IoDispatcher
import com.example.jaimequeraltgarrigos.spotify_artist.utils.Mapper
import kotlinx.coroutines.*

class ArtistRepositoryImpl(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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

                val artists: List<ArtistAPI> = withTimeout(5000) {
                    network.fetchArtists(query)
                }.artists.items.filter {
                    it.images?.isNotEmpty() == true
                }
                artistDao.insertArtists(Mapper.artistsAPIListToDBEntityList(artists))

                val albums: List<AlbumAPI> = artists.flatMap {
                    network.getAlbums(it.id).items
                }.distinctBy {
                    it.name
                }
                artistDao.insertAlbums(Mapper.albumAPIListToDBEntityList(albums))

                val songCrossRef = mutableListOf<AlbumSongCrossRef>()
                val songs: List<SongAPI> = albums.flatMap { album ->
                    val songsNetwork = network.getAlbumSongs(album.id).items
                    songsNetwork.forEach { song ->
                        songCrossRef.add(AlbumSongCrossRef(album.id, song.id))
                    }
                    songsNetwork
                }
                artistDao.insertSongsList(Mapper.songsAPIListToDBEntityList(songs))
                artistDao.insertAlbumSongCrossReferenceList(songCrossRef)

            } catch (error: Throwable) {
                throw ArtistError("Unable to fetch Artists ", error)
            }
        }
    }

    suspend fun cleanCache() {
        withContext(ioDispatcher) {
            artistDB.clearAllTables()
        }
    }
}
class ArtistError(message: String, cause: Throwable?) : Throwable(message, cause)