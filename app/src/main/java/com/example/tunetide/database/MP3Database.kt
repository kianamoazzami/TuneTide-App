package com.example.tunetide.database
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MP3File::class], version = 1)
abstract class MP3Database : RoomDatabase() {
    abstract fun mp3FileDao(): MP3FileDao
}