package com.example.tunetide.state

import com.example.tunetide.database.FilterType
import com.example.tunetide.database.Timer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

/**
 *  (1) temp data for a timer while it is being created
 *  (2) data concerning the state of all timers for the user
 */
data class TimerState @OptIn(ExperimentalTime::class) constructor(
    val timerName: String = "",
    val isInterval: Boolean = false,
    val numIntervals: Int = 1,
    val flowMusicId: Int = 0,
    val breakMusicId: Int = 0,
    val flowMusicDuration: Duration = 0.minutes,
    val breakMusicDuration: Duration = 0.minutes,
    val isSaved: Boolean = false,
    val isAddingTimer: Boolean = false,

    // not sure if this should be apart of this object ... easy to change later
    val timers: List<Timer> = emptyList(),
    val filterType: FilterType = FilterType.ALL
)
