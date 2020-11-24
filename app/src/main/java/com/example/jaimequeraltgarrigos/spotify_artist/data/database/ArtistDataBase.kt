package com.example.jaimequeraltgarrigos.spotify_artist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jaimequeraltgarrigos.spotify_artist.data.entity.db_entities.Album
import com.example.jaimequeraltgarrigos.spotify_artist.data.entity.db_entities.Artist

@Database(entities = [Artist::class, Album::class], version = 1, exportSchema = false)
abstract class ArtistDataBase : RoomDatabase() {
    abstract val artistDao: ArtistDao
}

private lateinit var INSTANCE: ArtistDataBase

/**
 * Instantiate a database from a context.
 */
fun getDatabase(context: Context): ArtistDataBase {
    synchronized(ArtistDataBase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    ArtistDataBase::class.java,
                    "artist_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}