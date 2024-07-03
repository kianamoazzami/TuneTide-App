package com.example.tunetide.database
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "MP3File",
    foreignKeys = [ForeignKey(
        entity = MP3Playlist::class,
        parentColumns = ["playlist_id"],
        childColumns = ["playlist_id"],
        onDelete = ForeignKey.NO_ACTION
    )])
data class MP3File (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "file_id")
    @NonNull
    val fileId: Int = 0,

    @ColumnInfo(name = "playlist_id")
    @NonNull
    val playlistId: Int,

    @ColumnInfo(name = "file_name")
    val fileName: String?,

    @ColumnInfo(name = "file_path")
    @NonNull
    val filePath: String,

    @ColumnInfo(name = "file_cover")
    val fileCover: String?
)
