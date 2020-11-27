package com.example.jaimequeraltgarrigos.spotify_artist.repository

interface ArtistRepository {
    suspend fun fetchArtist(query: String)
}