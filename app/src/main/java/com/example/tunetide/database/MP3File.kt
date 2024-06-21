package com.example.tunetide.database
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MP3Files")
data class MP3File (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "file_id")
    val fileId: Int = 0,
    @ColumnInfo(name = "file_name")
    @NonNull
    val fileName: String,
    @ColumnInfo(name = "file_path")
    @NonNull
    val filePath: String
)