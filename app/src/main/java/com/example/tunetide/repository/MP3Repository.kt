package com.example.tunetide.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tunetide.database.MP3File
import com.example.tunetide.database.MP3Playlist
import kotlinx.coroutines.flow.Flow

interface IMP3Repository {
    suspend fun insertFile(mp3File: MP3File)

    suspend fun deleteFile(mp3File: MP3File)

    fun getMP3Files(): Flow<List<MP3File>>

    fun getMP3FileById(fileId: Int): Flow<MP3File?>

    fun getMP3FileByPlaylist(playlistId: Int): Flow<List<MP3File>>

    suspend fun deleteFilesByPlaylistId(playlistId: Int)

    suspend fun insertPlaylist(mp3Playlist: MP3Playlist)

    suspend fun deletePlaylist(mp3Playlist: MP3Playlist)

    fun getMP3PlaylistById(playlistId: Int): Flow<MP3Playlist?>

    fun getMP3Playlists(): Flow<List<MP3Playlist>>
}

class MP3Repository(private val mP3Dao: MP3Dao) : IMP3Repository {
    // region Files ****************************************************************************
    override suspend fun insertFile(mp3File: MP3File) = mP3Dao.insertFile(mp3File)

    override suspend fun deleteFile(mp3File: MP3File) = mP3Dao.deleteFile(mp3File)

    override fun getMP3Files(): Flow<List<MP3File>> = mP3Dao.getMP3Files()

    override fun getMP3FileById(fileId: Int): Flow<MP3File?> = mP3Dao.getMP3FileById(fileId)

    override fun getMP3FileByPlaylist(playlistId: Int): Flow<List<MP3File>> = mP3Dao.getMP3FileByPlaylist(playlistId)

    override suspend fun deleteFilesByPlaylistId(playlistId: Int) = mP3Dao.deleteFilesByPlaylistId(playlistId)
    // endregion Files **************************************************************************

    // region Playlist **************************************************************************
    override suspend fun insertPlaylist(mp3Playlist: MP3Playlist) = mP3Dao.insertPlaylist(mp3Playlist)

    override suspend fun deletePlaylist(mp3Playlist: MP3Playlist) = mP3Dao.deletePlaylist(mp3Playlist)

    override fun getMP3PlaylistById(playlistId: Int): Flow<MP3Playlist?> = mP3Dao.getMP3PlaylistById(playlistId)

    override fun getMP3Playlists(): Flow<List<MP3Playlist>> = mP3Dao.getMP3Playlists()
    // endregion Playlist ***********************************************************************
}