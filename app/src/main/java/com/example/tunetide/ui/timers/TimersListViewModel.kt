package com.example.tunetide.ui.timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.FilterType
import com.example.tunetide.database.Timer
import com.example.tunetide.repository.TimerRepository
import com.example.tunetide.ui.timer.TimerUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TimersListViewModel (
    private val timerRepository: TimerRepository
): ViewModel() {


    // TODO @NOUR @KATHERINE inject into UI - reactive change (button changes data source (query))
    /*
    private val _filterType = MutableStateFlow(FilterType.ALL)
    private val _timers = _filterType
        .flatMapLatest { filterType ->
            when(filterType) {
                FilterType.ALL -> timerRepository.getTimers()
                FilterType.STANDARD -> timerRepository.getStandardTimers()
                FilterType.INTERVAL -> timerRepository.getIntervalTimers()
            }
        }
        // only show while user is active, default value is an empty list of timers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _timerUIState = MutableStateFlow(TimerUIState())

    // TODO @KATHERINE @MIA @NOUR figure out how to do this (have data that is changed based on
    //      button click (filters)
    // combine all flows into one flow; if any of these flows changes value, this code is executed
    val timersUIState: StateFlow<TimerUIState>
        = combine(_timerUIState, _filterType, _timers) { timersUIState, filterType, timers ->
        timersUIState.copy (
            timers = timers,
            filterType = filterType
        )
        // stopTimeout : (complex to understand) but this prevents a specific type of bug
    }.stateIn(scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
              initialValue = TimerUIState())

    fun filterTimers(filterType: FilterType) {
        // triggers change to map correct type of timers to our _timers Flow list
        _filterType.value = filterType
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    */
}