package com.example.jaimequeraltgarrigos.spotify_artist.data.network

import android.content.SharedPreferences
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.albums.AlbumsResponse
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.network_entities.artists.ArtistSearchResponse
import com.example.jaimequeraltgarrigos.spotify_artist.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASE_URL = "https://api.spotify.com/"
private val service: MainNetwork by lazy {

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(MyRetrofitInterceptor(TokenManager.token))
        .build()

    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val client: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MainNetwork::class.java)
}

fun getNetworkService() = service

interface MainNetwork {
    @GET("v1/search")
    suspend fun fetchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist",
        @Query("limit") limit: Int = 5
    ): ArtistSearchResponse

    @GET("v1/artists/{id}/albums")
    suspend fun getAlbums(@Path("id") id: String): AlbumsResponse
}

class MyRetrofitInterceptor(private val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (token != null) {
            val newRequest: Request = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return chain.proceed(newRequest)
        }
        return chain.proceed(originalRequest)
    }

}