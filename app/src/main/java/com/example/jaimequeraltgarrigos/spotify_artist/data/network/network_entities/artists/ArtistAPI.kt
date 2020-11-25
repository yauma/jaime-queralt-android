package com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists

import com.google.gson.annotations.SerializedName

data class ArtistAPI (
	@SerializedName("genres") val genres : List<String>,
	@SerializedName("href") val href : String,
	@SerializedName("id") val id : String,
	@SerializedName("images") val images : List<Images>,
	@SerializedName("name") val name : String,
	@SerializedName("popularity") val popularity : Int,
	@SerializedName("type") val type : String,
	@SerializedName("uri") val uri : String
)