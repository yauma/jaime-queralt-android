package com.example.jaimequeraltgarrigos.spotify_artist.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtistDao {
    @Transaction
    @Query("SELECT * FROM Artist")
    fun getArtistWithAlbum(): LiveData<List<ArtistWithAlbums>>

    @Transaction
    @Query("SELECT * FROM Artist")
    fun getArtistsWithAlbumAndSongs(): LiveData<List<ArtistWithAlbumsAndSongs>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<Artist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(artists: List<Album>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongsList(artists: List<Song>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbumSongCrossReference(albumSongCrossRef: AlbumSongCrossRef)
}