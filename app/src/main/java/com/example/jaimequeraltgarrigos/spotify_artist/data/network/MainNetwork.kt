package com.example.jaimequeraltgarrigos.spotify_artist.data.network

import androidx.lifecycle.LiveData
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.Album
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.spotify.com/"
private val service: MainNetwork by lazy {
    val token: String? =
        "BQA_drvnVjXvVTzznPfrRhtJgwioXnvk87DvwF5bqawghlxo5ndeIkcoQ1AhUWnoWIMLewqzs4V1PFI5V-ariT1QFHiKUjpMf6_EF8m2SbT6UjJmMVICrQl22dW01Bq8x4MvZt-Q2vmZm0ihYj0O5szU"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(MyRetrofitInterceptor(token))
        .build()

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
        @Query("type") type: String = "artist"
    ): List<NetworkArtist>

    @GET("artists/{id}/albums")
    suspend fun fetchAlbums(
        @Path("id") id: String
    ): List<Album>
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