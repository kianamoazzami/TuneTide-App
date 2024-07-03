package com.example.tunetide.database

import java.time.Month

enum class FilterType(val value: Int) {
    ALL(0),
    STANDARD(1),
    INTERVAL(2);

    companion object {
        fun fromValue(value: Int): FilterType? {
            return values().find { it.value == value }
        }
    }
}