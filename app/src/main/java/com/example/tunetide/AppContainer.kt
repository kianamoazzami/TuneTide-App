package com.example.tunetide

import android.content.Context
import com.example.tunetide.database.TuneTideDatabase
import com.example.tunetide.repository.MP3Repository
import com.example.tunetide.repository.PlaybackRepository
import com.example.tunetide.repository.SpotifyRepository
import com.example.tunetide.repository.TimerRepository

/**
 * dependency injection
 */
interface AppContainer {
    val timerRepository : TimerRepository
    val mp3Repository : MP3Repository
    val spotifyRepository: SpotifyRepository
    val playbackRepository: PlaybackRepository
}

/**
 * create instances of repositories within database context
 */
class AppDataContainer(private val context: Context): AppContainer {
    override val timerRepository: TimerRepository by lazy {
        TimerRepository(TuneTideDatabase.getDatabase(context).timerDao())
    }

    override val mp3Repository: MP3Repository by lazy {
        MP3Repository(TuneTideDatabase.getDatabase(context).mp3Dao())
    }

    override val spotifyRepository: SpotifyRepository by lazy {
        SpotifyRepository(TuneTideDatabase.getDatabase(context).spotifyDao())
    }

    override val playbackRepository: PlaybackRepository by lazy {
        PlaybackRepository(TuneTideDatabase.getDatabase(context).playbackDao())
    }
}