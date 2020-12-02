package com.example.jaimequeraltgarrigos.spotify_artist.data.database

import androidx.room.*

@Entity
data class Artist(
    @PrimaryKey val artistId: String,
    val artistName: String,
    val artistImageUrl: String
)

@Entity
data class Album(
    @PrimaryKey val albumId: String,
    val artistOwnerId: String,
    val albumName: String,
    val albumUrl: String
)

@Entity
data class Song(
    @PrimaryKey val songId: String,
    val songName: String,
    val artist: String,
    val length: String
)

@Entity(primaryKeys = ["albumId", "songId"])
data class AlbumSongCrossRef(
    val albumId: String,
    val songId: String
)

data class ArtistWithAlbums(
    @Embedded val artist: Artist,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "artistOwnerId"
    )
    var albums: List<Album>
)

data class AlbumWithSongs(
    @Embedded val album: Album,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "songId",
        associateBy = Junction(AlbumSongCrossRef::class)
    )
    var songs: List<Song>
)

data class ArtistWithAlbumsAndSongs(
    @Embedded val artist: Artist,
    @Relation(
        entity = Album::class,
        parentColumn = "artistId",
        entityColumn = "artistOwnerId"
    )
    var albums: List<AlbumWithSongs>
)