package com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists

import com.google.gson.annotations.SerializedName

data class Images (

	@SerializedName("height") val height : Int,
	@SerializedName("url") val url : String,
	@SerializedName("width") val width : Int
)