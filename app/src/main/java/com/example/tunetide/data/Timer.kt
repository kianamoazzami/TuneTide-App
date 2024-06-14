package com.example.tunetide.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.time.Duration

// (Mia June 14) No reference (here or throughout the object) for the music as I am unsure what
//               form /datatype that will take

// optional: can add sort order field (also must be done in DB)

/**
 * Data class for timer object
 */
@Entity(tableName = "timer")
data class Timer(
    // PK must still have default value (in this mapped DB object) to allow auto generation
    @ColumnInfo(name = "timer_id")
    @PrimaryKey(autoGenerate = true)
    val timerId: Int? = null,
    @ColumnInfo(name = "timer_name")
    @NonNull
    val timerName: String,
    @ColumnInfo(name = "is_interval")
    @NonNull
    val isInterval: Boolean,
    @ColumnInfo(name = "num_intervals")
    @NonNull
    @androidx.annotation.IntRange(from = 1, to = Long.MAX_VALUE)
    val numIntervals: Int,
    /**
    @ColumnInfo(name = "flow_music_id")
    @NonNull
    val flowMusicId: Int,
    @ColumnInfo(name = "break_music_id")
    @NonNull
    val breakMusicId: Int,
    **/
    @ColumnInfo(name = "flow_music_duration")
    @NonNull
    val flowMusicDuration: Duration,
    @ColumnInfo(name = "break_music_duration")
    @NonNull
    val breakMusicDuration: Duration,
    @ColumnInfo(name = "is_saved")
    @NonNull
    val isSaved: Boolean
)
