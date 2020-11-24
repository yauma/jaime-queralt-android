package com.example.jaimequeraltgarrigos.spotify_artist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jaimequeraltgarrigos.spotify_artist.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }
}