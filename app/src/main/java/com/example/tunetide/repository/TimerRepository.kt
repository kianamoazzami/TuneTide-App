package com.example.tunetide.repository

import androidx.room.Delete
import androidx.room.Query
import com.example.tunetide.database.Timer
import kotlinx.coroutines.flow.Flow

interface ITimerRepository {
    suspend fun upsertTimer(timer: Timer)

    suspend fun deleteTimer(timer: Timer)

    fun getTimerById(timerId: Int): Flow<Timer>

    fun getTimers(): Flow<List<Timer>>

    fun getIntervalTimers(): Flow<List<Timer>>

    fun getStandardTimers(): Flow<List<Timer>>
}

class TimerRepository(private val timerDao: TimerDao) : ITimerRepository {
    override suspend fun upsertTimer(timer: Timer) = timerDao.upsertTimer(timer)

    override suspend fun deleteTimer(timer: Timer) = timerDao.deleteTimer(timer)

    override fun getTimerById(timerId: Int): Flow<Timer> = timerDao.getTimerById(timerId)

    override fun getTimers(): Flow<List<Timer>> = timerDao.getTimers()

    override fun getIntervalTimers(): Flow<List<Timer>> = timerDao.getIntervalTimers()

    override fun getStandardTimers(): Flow<List<Timer>> = timerDao.getStandardTimers()
}