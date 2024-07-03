package com.example.tunetide.database

import java.time.Month

enum class MusicType(val value: Int) {
    MP3(0),
    SPOTIFY(1),
    NO_SOURCE(2);

    companion object {
        fun fromValue(value: Int): MusicType? {
            return values().find { it.value == value }
        }
    }
}