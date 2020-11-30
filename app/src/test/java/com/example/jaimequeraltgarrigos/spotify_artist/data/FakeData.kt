package com.example.jaimequeraltgarrigos.spotify_artist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Album
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Artist
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDao
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistWithAlbums
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumsResponse
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistSearchResponse
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Artists
import com.example.jaimequeraltgarrigos.spotify_artist.utils.Mapper
import kotlinx.coroutines.CompletableDeferred

class FakeData {
    companion object {
        val FAKE_ARTISTS = ArtistSearchResponse(
            Artists(
                "",
                listOf(
                    ArtistAPI(
                        emptyList(), "", "ARTISTID1", emptyList(),
                        "NAME1", 1, "", "URLID1"
                    ),
                    ArtistAPI(
                        emptyList(), "", "ARTISTID2", emptyList(),
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
        val FakeArtistWithAlbumList = listOf<ArtistWithAlbums>(
            ArtistWithAlbums(
                Mapper.artistAPIToDBEntity(FAKE_ARTISTS.artists.items[0]),
                Mapper.albumAPIListToDBEntityList(FAKE_ALBUMS.items)
            ),
            ArtistWithAlbums(
                Mapper.artistAPIToDBEntity(FAKE_ARTISTS.artists.items[1]),
                Mapper.albumAPIListToDBEntityList(FAKE_ALBUMS.items)
            )
        )
    }

    class FakeDao() : ArtistDao {
        var observableArtists = MutableLiveData<List<ArtistWithAlbums>>()
        private var artistsList = mutableListOf<ArtistWithAlbums>()

        override fun getArtistWithAlbum(): LiveData<List<ArtistWithAlbums>> {
            return observableArtists
        }

        override suspend fun insertArtists(artists: List<Artist>) {
            artistsList = artists.map {
                ArtistWithAlbums(it, emptyList<Album>())
            }.toMutableList()
            observableArtists.value = artistsList
        }

        override suspend fun insertAlbums(albums: List<Album>) {
            artistsList.forEach() {
                it.albums = albums
            }
            observableArtists.value = artistsList
        }

        suspend fun clearCache() {
            artistsList = mutableListOf()
            observableArtists.value = artistsList
        }

    }

    open class FakeNetwork(
        private val artistSearchResponse: ArtistSearchResponse,
        private val albumsResponse: AlbumsResponse
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
    }

    class FakeDataCompletableSource(
        private val artistSearchResponse: ArtistSearchResponse,
        private val albumsResponse: AlbumsResponse
    ) :
        FakeNetwork(artistSearchResponse, albumsResponse) {
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
    }

}