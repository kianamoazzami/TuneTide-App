package com.example.tunetide.database
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.reflect.KClass

@Entity(tableName = "MP3File")
data class MP3File (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "file_id")
    val fileId: Int = 0,
    @ColumnInfo(name = "playlist_id")
    //@ForeignKey(MP3Playlist.class, ["file_id"], ["playlist_id"], ForeignKey.CASCADE, ForeignKey.NO_ACTION, false)
    @NonNull
    val playlistId: Int,
    @ColumnInfo(name = "file_name")
    @NonNull
    val fileName: String,
    @ColumnInfo(name = "file_path")
    @NonNull
    val filePath: String,
    @ColumnInfo(name = "file_cover")
    val fileCover: String
)