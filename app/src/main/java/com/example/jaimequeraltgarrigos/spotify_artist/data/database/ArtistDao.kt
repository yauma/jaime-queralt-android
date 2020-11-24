package com.example.jaimequeraltgarrigos.spotify_artist.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.jaimequeraltgarrigos.spotify_artist.data.entity.db_entities.ArtistWithAlbums
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Transaction
    @Query("SELECT * FROM Artist")
    fun getArtistWithAlbum(): Flow<List<ArtistWithAlbums>>
}