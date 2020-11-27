package com.example.jaimequeraltgarrigos.spotify_artist.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.HttpException
import java.io.IOException

fun asNetworkException(ex: Throwable): String {
    return when (ex) {
        is IOException -> "No Internet Connection"
        else -> "Something went wrong..."
    }
}

