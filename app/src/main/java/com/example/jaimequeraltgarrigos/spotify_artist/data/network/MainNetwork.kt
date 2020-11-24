package com.example.jaimequeraltgarrigos.spotify_artist.data.network

import com.example.jaimequeraltgarrigos.spotify_artist.data.entity.db_entities.Album
import com.example.jaimequeraltgarrigos.spotify_artist.data.entity.network_entitites.NetworkArtist
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.spotify.com/v1"
private val service: MainNetwork by lazy {

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MainNetwork::class.java)
}

fun getNetworkService() = service

interface MainNetwork {
    @GET("/search")
    suspend fun fetchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist"
    ): Flow<List<NetworkArtist>>

    @GET("artists/{id}/albums")
    suspend fun fetchAbums(
        @Path("id") id: String
    ): Flow<List<Album>>
}