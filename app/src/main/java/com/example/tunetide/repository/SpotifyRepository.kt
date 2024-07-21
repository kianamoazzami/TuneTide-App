package com.example.tunetide.repository

import androidx.room.Delete
import androidx.room.Query
import com.example.tunetide.database.MP3File
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.database.SpotifyPlaylist
import kotlinx.coroutines.flow.Flow

interface ISpotifyRepository {
    suspend fun insertPlaylist(spotifyPlaylist: SpotifyPlaylist)

    suspend fun deletePlaylist(spotifyPlaylist: SpotifyPlaylist)

    fun getSpotifyPlaylists(): Flow<List<SpotifyPlaylist>>

    fun getSpotifyPlaylistById(playlistId: Int): Flow<SpotifyPlaylist?>
}

class SpotifyRepository(private val spotifyDao: SpotifyDao): ISpotifyRepository {
    override suspend fun insertPlaylist(spotifyPlaylist: SpotifyPlaylist)
        = spotifyDao.insertPlaylist(spotifyPlaylist)

    override suspend fun deletePlaylist(spotifyPlaylist: SpotifyPlaylist)
        = spotifyDao.deletePlaylist(spotifyPlaylist)

    override fun getSpotifyPlaylists(): Flow<List<SpotifyPlaylist>>
        = spotifyDao.getSpotifyPlaylists()

    override fun getSpotifyPlaylistById(playlistId: Int): Flow<SpotifyPlaylist?>
            = spotifyDao.getSpotifyPlaylistById(playlistId)
}