package com.example.jaimequeraltgarrigos.spotify_artist.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtistDao {
    @Transaction
    @Query("SELECT * FROM Artist")
    fun getArtistWithAlbum(): LiveData<List<ArtistWithAlbums>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<Artist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(artists: List<Album>)
}