package com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.songs


import com.google.gson.annotations.SerializedName

data class SongsResponse(
    @SerializedName("items")
    val items: List<SongAPI> = listOf(),
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("next")
    val next: String = "",
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("previous")
    val previous: String = "",
    @SerializedName("total")
    val total: Int = 0
)