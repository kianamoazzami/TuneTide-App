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
import com.example.tunetide.ui.timer.TimerDetails
import com.example.tunetide.ui.timer.toTimerDetails
import com.example.tunetide.ui.timer.toTimerUIState
import kotlinx.coroutines.flow.filterNotNull

class TimersListViewModel (
    private val timerRepository: TimerRepository
): ViewModel() {

    // NOTE: may cause runtime errors (init not surrounded by coroutine)
    private val _filterType = MutableStateFlow(FilterType.ALL)

    private val _timers: StateFlow<List<Timer>> = _filterType
        .flatMapLatest { filterType ->
            when(filterType) {
                FilterType.ALL -> timerRepository.getTimers().filterNotNull()
                FilterType.STANDARD -> timerRepository.getStandardTimers().filterNotNull()
                FilterType.INTERVAL -> timerRepository.getIntervalTimers().filterNotNull()
            }
        }
        // only show while user is active, default value is an empty list of timers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _timerListUIState = MutableStateFlow(TimerListUIState())

    // combine all flows into one State flow; if any of these flows changes value, this code is executed
    val timerListUIState: StateFlow<TimerListUIState>
        = combine(_timerListUIState, _timers, _filterType) { timerListUIState, timers, filterType ->
            timerListUIState.copy (
                timers = timers,
                filterType = filterType
            )
        }.stateIn(scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
          initialValue = TimerListUIState())

    fun filterTimers(filterType: FilterType) {
        // triggers change to map correct type of timers to our _timers Flow list
        _filterType.value = filterType
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

/**
 *  UI state for the list of timers
 */
data class TimerListUIState(
    val timers: List<Timer> = listOf(),
    val filterType: FilterType = FilterType.ALL
)
