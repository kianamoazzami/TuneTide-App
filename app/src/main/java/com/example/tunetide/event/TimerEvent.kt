package com.example.tunetide.event

import com.example.tunetide.database.FilterType
import com.example.tunetide.database.Timer
import kotlin.time.Duration

sealed interface TimerEvent {
    object SaveTimer: TimerEvent

    object ShowDialog: TimerEvent
    object HideDialog: TimerEvent

    data class setTimerName(val timerName: String): TimerEvent
    data class setIsInterval(val isInterval: Boolean): TimerEvent
    data class setNumIntervals(val numIntervals: Int): TimerEvent
    // TODO setting the references to what music is played
    data class setFlowMusicDuration(val flowMusicDuration: Duration): TimerEvent
    data class setBreakMusicDuration(val breakMusicDuration: Duration): TimerEvent
    data class setIsSaved(val isSaved: Boolean): TimerEvent

    data class deleteTimer(val timer: Timer): TimerEvent

    data class filterTimers(val filterType: FilterType): TimerEvent
}