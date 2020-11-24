package com.example.jaimequeraltgarrigos.spotify_artist.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Artist(
    @PrimaryKey val artistId: Long,
    val artistName: String,
    val artistImageUrl: String
)

@Entity
data class Album(
    @PrimaryKey val albumId: Long,
    val artistOwnerId: Long,
    val albumName: String,
    val albumUrl: String
)

data class ArtistWithAlbums(
    @Embedded val artist: Artist,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "artistOwnerId"
    )
    val albums: List<Album>
)