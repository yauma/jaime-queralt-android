package com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs


import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistAPI
import com.google.gson.annotations.SerializedName

data class SongAPI(
    @SerializedName("disc_number")
    val discNumber: Int = 0,
    @SerializedName("duration_ms")
    val durationMs: Long = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
)