package com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums

import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Artist
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.Images
import com.google.gson.annotations.SerializedName

data class Album (

	@SerializedName("album_group") val album_group : String,
	@SerializedName("album_type") val album_type : String,
	@SerializedName("artists") val artists : List<Artist>,
	@SerializedName("href") val href : String,
	@SerializedName("id") val id : String,
	@SerializedName("images") val images : List<Images>,
	@SerializedName("name") val name : String,
	@SerializedName("release_date") val release_date : String,
	@SerializedName("release_date_precision") val release_date_precision : String,
	@SerializedName("total_tracks") val total_tracks : Int,
	@SerializedName("type") val type : String,
	@SerializedName("uri") val uri : String
)