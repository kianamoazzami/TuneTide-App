package com.example.tunetide.ui

import kotlin.time.Duration

/**
 * Data class to hold state of current running timer
 */

// (Mia: June 10) I am not sure if this is the best way to hold this data before it is held as
//                a token (to persist), but I put this here for now
data class CurrentTimerState(
    val timer_id: Int,
    val state: String, // "flow" or "break" OR we can use Int with 1 and 2
    val current_interval: Int,
    val current_interval_remaining_duration: Duration
)
