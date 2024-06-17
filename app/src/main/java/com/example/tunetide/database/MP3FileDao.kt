package com.example.tunetide.database
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MP3FileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mp3File: MP3File)

    @Delete
    suspend fun delete(mp3File: MP3File)

    @Query("SELECT * FROM MP3Files")
    suspend fun getAll(): List<MP3File>
}