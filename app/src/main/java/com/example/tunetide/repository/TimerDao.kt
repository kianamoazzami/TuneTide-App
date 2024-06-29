package com.example.tunetide.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tunetide.database.Timer
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for timer database direct interface (CRUD)
 */
@Dao
interface TimerDao {

    // region CUD
    @Upsert
    suspend fun upsertTimer(timer: Timer)

    @Delete
    suspend fun deleteTimer(timer: Timer)
    // endregion

    // region READ
    @Query("SELECT * FROM timer WHERE timer_id == :timerId")
    fun getTimerById(timerId: Int): Flow<Timer>

    @Query("SELECT * FROM timer WHERE is_saved ORDER BY timer_name ASC")
    fun getTimers(): Flow<List<Timer>>

    @Query("SELECT * FROM timer WHERE is_saved AND is_interval ORDER BY timer_name ASC")
    fun getIntervalTimers(): Flow<List<Timer>>

    @Query("SELECT * FROM timer WHERE is_saved AND NOT is_interval ORDER BY timer_name ASC")
    fun getStandardTimers(): Flow<List<Timer>>
    // endregion
}