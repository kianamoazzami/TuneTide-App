package com.example.tunetide.ui.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.tunetide.repository.TimerRepository

class TimerEditViewModel (
    private val savedStateHandle: SavedStateHandle,
    private val timerRepository: TimerRepository
): ViewModel() {
}