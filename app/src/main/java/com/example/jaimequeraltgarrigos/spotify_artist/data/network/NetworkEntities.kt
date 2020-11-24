package com.example.jaimequeraltgarrigos.spotify_artist.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class NetworkArtist(
    @SerializedName("id")
    @Expose()
    val id: Long,
    @SerializedName("name")
    @Expose()
    val name: String,
    @SerializedName("images")
    @Expose()
    val images: List<NetworkImages>?,
    val albums: List<NetworkAlbum>?
)

data class NetworkImages(
    @SerializedName("height")
    @Expose()
    val height: Int,
    @SerializedName("width")
    @Expose()
    val width: Int,
    @SerializedName("url")
    @Expose()
    val url: String
)

data class NetworkAlbum(
    @SerializedName("id")
    @Expose()
    val id: Long,
    @SerializedName("artists")
    @Expose()
    val artists: List<NetworkArtist>,
    @SerializedName("name")
    @Expose()
    val name: String,
    @SerializedName("images")
    @Expose()
    val images: List<NetworkImages>?
)