package com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities

import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs.SongAPI

data class ArtistsWithAlbumAndSongsAPI(private val artist: ArtistAPI,
private val albums: List<AlbumWithSongsAPI>)

class AlbumWithSongsAPI(val album: AlbumAPI, val songs: List<SongAPI> = emptyList()) {

}
