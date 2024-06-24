package com.example.tunetide.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SpotifyPlaylist")
data class SpotifyPlaylist(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    val playlistId: Int = 0,
    @ColumnInfo(name = "playlist_name")
    val playlistName: String,
    @ColumnInfo(name = "uri_path")
    @NonNull
    val uriPath: String,
    @ColumnInfo(name = "playlist_cover")
    val playlistCover: String
)
