package com.example.tunetide.data

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for timer database direct interface (CRUD)
 */
interface TimerDao {

    // region CUD
    @Upsert
    suspend fun upsertTimer(timer: Timer)

    @Delete
    suspend fun deleteTimer(timer: Timer)
    // endregion

    // region READ
    @Query("SELECT * FROM timer WHERE is_saved ORDER BY timer_name ASC")
    fun getTimers(): Flow<List<Timer>>

    @Query("SELECT * FROM timer WHERE is_saved AND interval ORDER BY timer_name ASC")
    fun getIntervalTimers(): Flow<List<Timer>>

    @Query("SELECT * FROM timer WHERE is_saved AND NOT interval ORDER BY timer_name ASC")
    fun getStandardTimers(): Flow<List<Timer>>
    // endregion
}