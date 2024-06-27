package com.example.tunetide.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO @MIA do i even need a remaining seconds ... I think yes (for when app is closed)

/**
 * Used to save the information about the current running timer when the app is closed
 */
@Entity(tableName = "Playback")
data class Playback(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1, // singleton table (only one running timer (row))
    @ColumnInfo(name = "timer_id")
    val timerId: Int,

    // TODO @ERICA @KIANA no reference of track, where in the track it was left off, etc is here
    //      that information seems to be handled by your classes, not integrated here (I am not sure)

    @ColumnInfo(name = "state_type")
    val stateType: Int, // mapped in from an Integer in DB
    @ColumnInfo(name = "current_interval")
    val currentInterval: Int,
    @ColumnInfo(name = "current_interval_remaining_seconds")
    @androidx.annotation.IntRange(from = 0, to = Long.MAX_VALUE)
    val currentIntervalRemainingSeconds: Int, // seconds
    @ColumnInfo(name = "is_playing")
    val isPlaying: Boolean = false,
)
