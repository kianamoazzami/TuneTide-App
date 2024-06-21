package com.example.tunetide.state

import kotlin.time.Duration

/**
 * Data class to hold state of current running timer
 */

data class CurrentTimerState(
    val timerId: Int,
    val state: String, // "flow" or "break" OR we can use Int with 1 and 2
    val currentInterval: Int,
    val currentIntervalRemainingDuration: Duration,
    val isFinished: Boolean
)
