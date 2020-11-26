package com.example.jaimequeraltgarrigos.spotify_artist.utils

import android.widget.SearchView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun SearchView.getQueryTextChangeStateFlow(): Flow<String> {

    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })

    return query
}