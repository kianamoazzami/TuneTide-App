package com.example.tunetide.ui.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.tunetide.repository.TimerRepository

class TimerEditViewModel (
    savedStateHandle: SavedStateHandle,
    private val timerRepository: TimerRepository
): ViewModel() {
}