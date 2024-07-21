package com.example.tunetide.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tunetide.database.MP3File
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.database.SpotifyPlaylist
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotifyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(spotifyPlaylist: SpotifyPlaylist)

    @Delete
    suspend fun deletePlaylist(spotifyPlaylist: SpotifyPlaylist)

    @Query("SELECT * FROM SpotifyPlaylist")
    fun getSpotifyPlaylists(): Flow<List<SpotifyPlaylist>>

    @Query("SELECT * FROM SpotifyPlaylist WHERE playlist_id == :playlistId")
    fun getSpotifyPlaylistById(playlistId: Int): Flow<SpotifyPlaylist?>
}