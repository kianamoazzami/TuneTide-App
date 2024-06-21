package com.example.tunetide.event

import kotlin.time.Duration

sealed interface CurrentTimerEvent {
    object PauseTimer: CurrentTimerEvent

    object PlayTimer: CurrentTimerEvent

    object SkipSong: CurrentTimerEvent

    object CompleteInterval: CurrentTimerEvent
}