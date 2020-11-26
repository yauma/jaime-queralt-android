package com.example.jaimequeraltgarrigos.spotify_artist.utils

import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Album
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Artist
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Artists

class Mapper {
    companion object {
        fun artistAPIToDBEntity(artist: ArtistAPI): Artist {
            val url: String = try {
                artist.images?.get(0)?.url ?: ""
            } catch (e: Exception){
                ""
            }
            return Artist(artist.id, artist.name, url)
        }

        //TODO test with multiple Artists ids
        fun albumAPIToDBEntity(album: AlbumAPI): Album {
            val url: String = try {
                album.images?.get(0)?.url ?: ""
            } catch (e: Exception){
                ""
            }
            return Album(
                album.id,
                album.artistAPIS[0]?.id ?: "no artist information",
                album.name,
                url
            )
        }

        fun artistsAPIListToDBEntityList(artists: List<ArtistAPI>): List<Artist> {
            return artists.map {
                artistAPIToDBEntity(
                    it
                )
            }
        }

        fun albumAPIListToDBEntityList(albums: List<AlbumAPI>): List<Album> {
            return albums.map {
                albumAPIToDBEntity(
                    it
                )
            }
        }
    }
}