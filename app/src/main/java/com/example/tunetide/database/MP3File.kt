package com.example.tunetide.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MP3Files")
data class MP3File (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val filePath: String
)