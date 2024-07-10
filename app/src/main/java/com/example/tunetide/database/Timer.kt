package com.example.tunetide.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for timer object
 */
@Entity(tableName = "Timer")
data class Timer(
    @ColumnInfo(name = "timer_id")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val timerId: Int,

    @ColumnInfo(name = "timer_name")
    @NonNull
    val timerName: String,

    @ColumnInfo(name = "is_interval")
    @NonNull
    val isInterval: Boolean,

    @ColumnInfo(name = "num_intervals")
    @NonNull
    //@androidx.annotation.IntRange(from = 1, to = Long.MAX_VALUE)
    val numIntervals: Int,

    @ColumnInfo(name = "flow_music_duration_seconds")
    @NonNull
    //@androidx.annotation.IntRange(from = 0, to = Long.MAX_VALUE)
    val flowMusicDurationSeconds: Int, // seconds

    @ColumnInfo(name = "spotify_flow_music_playlist_id")
    @NonNull
    val spotifyFlowMusicPlaylistId: Int,

    @ColumnInfo(name = "mp3_flow_music_playlist_id")
    @NonNull
    val mp3FlowMusicPlaylistId: Int,

    @ColumnInfo(name = "break_music_duration_seconds")
    @NonNull
    //@androidx.annotation.IntRange(from = 1, to = Long.MAX_VALUE)
    val breakMusicDurationSeconds: Int, // seconds

    @ColumnInfo(name = "spotify_break_music_playlist_id")
    @NonNull
    val spotifyBreakMusicPlaylistId: Int,

    @ColumnInfo(name = "mp3_break_music_playlist_id")
    @NonNull
    val mp3BreakMusicPlaylistId: Int,

    @ColumnInfo(name = "is_saved")
    @NonNull
    val isSaved: Boolean
)