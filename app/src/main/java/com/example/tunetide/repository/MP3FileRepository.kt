package com.example.tunetide.repository

import androidx.room.Delete
import androidx.room.Query
import com.example.tunetide.database.MP3File
import kotlinx.coroutines.flow.Flow

interface IMP3FileRepository {
    suspend fun insert(mp3File: MP3File)

    suspend fun delete(mp3File: MP3File)

    fun getMP3Files(): Flow<List<MP3File>>

    fun getMP3FileById(id: Int): Flow<MP3File?>
}

class MP3FileRepository(private val mP3FileDao: MP3FileDao) : IMP3FileRepository {
    override suspend fun insert(mp3File: MP3File) = mP3FileDao.insert(mp3File)

    override suspend fun delete(mp3File: MP3File) = mP3FileDao.delete(mp3File)

    override fun getMP3Files(): Flow<List<MP3File>> = mP3FileDao.getMP3Files()

    override fun getMP3FileById(fileId: Int): Flow<MP3File?> = mP3FileDao.getMP3FileById(fileId)
}