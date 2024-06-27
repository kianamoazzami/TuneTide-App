package com.example.tunetide.database

import java.time.Month

enum class StateType(val value: Int){
    FLOW(0),
    BREAK(1),
    NONE(2);

    companion object {
        fun fromValue(value: Int): StateType? {
            return values().find { it.value == value }
        }
    }
}