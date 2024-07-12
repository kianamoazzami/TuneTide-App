package com.example.tunetide.repository
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tunetide.database.MP3File
import com.example.tunetide.database.MP3Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface MP3Dao {
    // region Files ****************************************************************************
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(mp3File: MP3File)

    @Delete
    suspend fun deleteFile(mp3File: MP3File)

    @Query("SELECT * FROM MP3File")
    fun getMP3Files(): Flow<List<MP3File>>

    @Query("SELECT * FROM MP3File WHERE file_id == :fileId")
    fun getMP3FileById(fileId: Int): Flow<MP3File?>

    @Query("SELECT * FROM MP3File WHERE playlist_id == :playlistId")
    fun getMP3FileByPlaylist(playlistId: Int): Flow<List<MP3File>>

    @Query("DELETE FROM MP3File WHERE playlist_id == :playlistId")
    suspend fun deleteFilesByPlaylistId(playlistId: Int)
    // endregion Files *************************************************************************

    // region Playlist *************************************************************************
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(mp3Playlist: MP3Playlist)

    @Delete
    suspend fun deletePlaylist(mp3Playlist: MP3Playlist)

    @Query("SELECT * FROM MP3Playlist WHERE playlist_id == :playlistId")
    fun getMP3PlaylistById(playlistId: Int): Flow<MP3Playlist?>

    @Query("SELECT * FROM MP3Playlist")
    fun getMP3Playlists(): Flow<List<MP3Playlist>>
    // endregion Playlist **********************************************************************
}