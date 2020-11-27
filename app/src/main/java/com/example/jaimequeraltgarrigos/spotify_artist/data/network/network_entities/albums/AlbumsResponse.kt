package com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums

import com.google.gson.annotations.SerializedName

data class AlbumsResponse (

    @SerializedName("href") val href : String,
    @SerializedName("items") val items : List<AlbumAPI>,
    @SerializedName("limit") val limit : Int,
    @SerializedName("next") val next : String,
    @SerializedName("offset") val offset : Int,
    @SerializedName("previous") val previous : String,
    @SerializedName("total") val total : Int
)