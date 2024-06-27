package com.example.tunetide.ui.timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.FilterType
import com.example.tunetide.event.TimerEvent
import com.example.tunetide.repository.TimerRepository
import com.example.tunetide.event.TimerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class TimersListViewModel (
    private val timerRepository: TimerRepository
): ViewModel() {

    private val _filterType = MutableStateFlow(FilterType.ALL)

    // reactive change (button changes data source (query))
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

    private val _state = MutableStateFlow(TimerState())
    // combine all flows into one flow; if any of these flows changes value, this code is executed
    val state = combine(_state, _filterType, _timers) { state, filterType, timers ->
        state.copy (
            timers = timers,
            filterType = filterType
        )
        // stopTimeout : (complex to understand) but this prevents a specific type of bug
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), TimerState())

    fun onEvent(event: TimerEvent) {
        when(event) {
            is TimerEvent.filterTimers -> {
                // triggers change to map correct type of timers to our _timers Flow list
                _filterType.value = event.filterType
            }
            else -> {}
        }
    }
}