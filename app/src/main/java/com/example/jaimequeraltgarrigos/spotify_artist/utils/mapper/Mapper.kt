package com.example.jaimequeraltgarrigos.spotify_artist.utils.mapper

import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Album
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Artist
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Artists

class Mapper {
    companion object {
        fun artistAPIToDBEntity(artist: ArtistAPI): Artist {
            return Artist(artist.id, artist.name, artist.images[0].url)
        }

        //TODO test with multiple Artists ids
        fun albumAPIToDBEntity(album: AlbumAPI): Album {
            return Album(album.id, album.artistAPIS[0].id, album.name, album.images[0].url)
        }

        fun artistsAPIListToDBEntityList(artists: List<ArtistAPI>): List<Artist> {
            return artists.map {
                artistAPIToDBEntity(it)
            }
        }

        fun albumAPIListToDBEntityList(albums: List<AlbumAPI>): List<Album> {
            return albums.map {
                albumAPIToDBEntity(it)
            }
        }
    }
}