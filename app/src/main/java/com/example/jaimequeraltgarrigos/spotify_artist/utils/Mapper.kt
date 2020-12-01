package com.example.jaimequeraltgarrigos.spotify_artist.utils

import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Album
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Artist
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistWithAlbumsAndSongs
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Song
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.ArtistsWithAlbumAndSongsAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Artists
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs.SongAPI

class Mapper {
    companion object {
        fun artistAPIToDBEntity(artist: ArtistAPI): Artist {
            var url = ""
            if (artist.images != null && artist.images.isNotEmpty()) {
                url = artist.images[1].url
            }
            return Artist(artist.id, artist.name, url)
        }

        //TODO Make resilent to nulls
        fun albumAPIToDBEntity(album: AlbumAPI): Album {
            var url = ""
            if (album.images != null && album.images.isNotEmpty()) {
                url = album.images[1].url
            }
            return Album(
                album.id,
                album.artistAPIS[0]?.id ?: "no artist information",
                album.name,
                url
            )
        }

        fun songApiTODBEntity(song: SongAPI): Song {
            return Song(song.id, song.name, "", song.durationMs.toString())
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

        fun songsAPIListToDBEntityList(songs: List<SongAPI>): List<Song> {
            return songs.map {
                songApiTODBEntity(
                    it
                )
            }
        }

    }
}