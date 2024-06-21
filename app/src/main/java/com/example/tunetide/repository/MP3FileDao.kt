package com.example.tunetide.repository
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tunetide.database.MP3File
import kotlinx.coroutines.flow.Flow

@Dao
interface MP3FileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mp3File: MP3File)

    @Delete
    suspend fun delete(mp3File: MP3File)

    @Query("SELECT * FROM MP3Files")
    fun getMP3Files(): Flow<List<MP3File>>

    @Query("SELECT file_path FROM MP3Files WHERE file_id == :fileId")
    fun getMP3FileById(fileId: Int): Flow<MP3File?>
}