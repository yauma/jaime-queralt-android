package com.example.jaimequeraltgarrigos.spotify_artist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.*
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumsResponse
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistSearchResponse
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Artists
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Images
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs.SongAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs.SongsResponse
import com.example.jaimequeraltgarrigos.spotify_artist.utils.Mapper
import kotlinx.coroutines.CompletableDeferred

class FakeData {
    companion object {
        val FAKE_ARTISTS = ArtistSearchResponse(
            Artists(
                "",
                listOf(
                    ArtistAPI(
                        emptyList(), "", "ARTISTID1", listOf(Images(1, "", 1), Images(1, "", 1)),
                        "NAME1", 1, "", "URLID1"
                    ),
                    ArtistAPI(
                        emptyList(), "", "ARTISTID2", listOf(Images(1, "", 1), Images(1, "", 1)),
                        "NAME2", 1, "", "URLID2"
                    )
                ), 1, "", 1, "", 1
            )
        )
        val FAKE_ALBUMS = AlbumsResponse(
            "",
            listOf(
                AlbumAPI(
                    "", "", listOf(FAKE_ARTISTS.artists.items[0]),
                    "", "ALBUMID1", emptyList(), "ALBUMNAME1", "",
                    "", 1, "", "URLID1"
                ),
                AlbumAPI(
                    "", "", listOf(FAKE_ARTISTS.artists.items[1]),
                    "", "ALBUMID2", emptyList(), "ALBUMNAME2", "",
                    "", 1, "", "URLID2"
                )
            ), 1, "", 1, "", 1
        )

        val FAKE_SONGS = SongsResponse(
            listOf(
                SongAPI(1, 1, "SONGID1", "SONG1"),
                SongAPI(1, 1, "SONGID2", "SONG2")
            )
        )
        val FAKE_ALBUMS_WITH_SONGS = listOf<AlbumWithSongs>(
            AlbumWithSongs(
                Mapper.albumAPIToDBEntity(FAKE_ALBUMS.items[0]),
                Mapper.songsAPIListToDBEntityList(FAKE_SONGS.items)
            ),
            AlbumWithSongs(
                Mapper.albumAPIToDBEntity(FAKE_ALBUMS.items[1]),
                Mapper.songsAPIListToDBEntityList(FAKE_SONGS.items)
            )
        )
    }

    class FakeDao() : ArtistDao {
        var observableArtists = MutableLiveData<List<ArtistWithAlbumsAndSongs>>()
        private var artistsList = listOf<Artist>()
        private var albumList = listOf<AlbumWithSongs>()
        private var songsList = mutableListOf<Song>()


        override fun getArtistWithAlbum(): LiveData<List<ArtistWithAlbums>> {
            return MutableLiveData<List<ArtistWithAlbums>>()
        }

        override fun getArtistsWithAlbumAndSongs(): LiveData<List<ArtistWithAlbumsAndSongs>> {
            return observableArtists
        }

        override suspend fun insertArtists(artists: List<Artist>) {
            observableArtists.postValue(artists.map {
                ArtistWithAlbumsAndSongs(it, emptyList())
            })
        }

        override suspend fun insertAlbums(albums: List<Album>) {
            albumList = albums.map {
                AlbumWithSongs(it, emptyList())
            }
            observableArtists.value?.forEach {
                it.albums = albumList
            }
        }

        override suspend fun insertSongsList(songs: List<Song>) {
            albumList.forEach {
                it.songs = songs
            }
            observableArtists.value?.forEach {
                it.albums = albumList
            }
        }

        override suspend fun insertAlbumSongCrossReferenceList(albumSongCrossRef: List<AlbumSongCrossRef>) {

        }

        override suspend fun insertAlbumSongCrossReference(albumSongCrossRef: AlbumSongCrossRef) {

        }

        suspend fun clearCache() {
            artistsList = mutableListOf()
            observableArtists.value = emptyList()
        }

    }

    open class FakeNetwork(
        private val artistSearchResponse: ArtistSearchResponse,
        private val albumsResponse: AlbumsResponse,
        private val songsResponse: SongsResponse
    ) : MainNetwork {
        override suspend fun fetchArtists(
            query: String,
            type: String,
            limit: Int
        ): ArtistSearchResponse {
            return artistSearchResponse
        }

        override suspend fun getAlbums(id: String): AlbumsResponse {
            return albumsResponse
        }

        override suspend fun getAlbumSongs(id: String): SongsResponse {
            return songsResponse
        }
    }

    class FakeDataCompletableSource(
        private val artistSearchResponse: ArtistSearchResponse,
        private val albumsResponse: AlbumsResponse,
        private val songsResponse: SongsResponse
    ) :
        FakeNetwork(artistSearchResponse, albumsResponse, songsResponse) {
        private var completable = CompletableDeferred<ArtistSearchResponse>()
        override suspend fun fetchArtists(
            query: String,
            type: String,
            limit: Int
        ): ArtistSearchResponse {
            return completable.await()
        }
    }

    /**
     * Testing Fake for MainNetwork that lets you complete or error all current requests
     */
    class MainNetworkCompletableFake() : MainNetwork {
        private var artistsCompletable = CompletableDeferred<ArtistSearchResponse>()
        private var albumsCompletable = CompletableDeferred<AlbumsResponse>()
        private var songsCompletable = CompletableDeferred<SongsResponse>()

        fun sendCompletionToAllArtistsCurrentRequests(result: ArtistSearchResponse) {
            artistsCompletable.complete(result)
            artistsCompletable = CompletableDeferred()
        }

        fun sendErrorToArtistsCurrentRequests(throwable: Throwable) {
            artistsCompletable.completeExceptionally(throwable)
            artistsCompletable = CompletableDeferred()
        }

        override suspend fun fetchArtists(
            query: String,
            type: String,
            limit: Int
        ): ArtistSearchResponse {
            return artistsCompletable.await()
        }

        override suspend fun getAlbums(id: String): AlbumsResponse {
            return albumsCompletable.await()
        }

        override suspend fun getAlbumSongs(id: String): SongsResponse {
            return songsCompletable.await()
        }
    }

}