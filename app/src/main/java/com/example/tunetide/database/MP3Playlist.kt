package com.example.tunetide.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MP3Playlist")
data class MP3Playlist(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playlist_id")
    @NonNull
    val playlistId: Int = 0,

    @ColumnInfo(name = "playlist_name")
    @NonNull
    val playlistName: String,

    @ColumnInfo(name = "uri_path")
    @NonNull
    val uriPath: String,

    @ColumnInfo(name = "playlist_cover")
    val playlistCover: String?,

    @ColumnInfo(name = "is_saved")
    @NonNull
    val isSaved: Boolean
)
