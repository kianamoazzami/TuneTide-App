package com.example.tunetide

import android.content.Context
import com.example.tunetide.database.TuneTideDatabase
import com.example.tunetide.repository.MP3FileRepository
import com.example.tunetide.repository.TimerRepository

/**
 * dependency injection
 */
interface AppContainer {
    val timerRepository : TimerRepository
    val mp3FileRepository : MP3FileRepository
}

/**
 * create instances of repositories within database context
 */
class AppDataContainer(private val context: Context): AppContainer {
    override val timerRepository: TimerRepository by lazy {
        TimerRepository(TuneTideDatabase.getDatabase(context).timerDao())
    }
    override val mp3FileRepository: MP3FileRepository by lazy {
        MP3FileRepository(TuneTideDatabase.getDatabase(context).mp3FileDao())
    }
}