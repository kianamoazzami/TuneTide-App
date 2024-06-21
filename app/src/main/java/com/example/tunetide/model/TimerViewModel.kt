package com.example.tunetide.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.FilterType
import com.example.tunetide.database.Timer
import com.example.tunetide.repository.TimerDao
import com.example.tunetide.event.TimerEvent
import com.example.tunetide.state.TimerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

/**
 * View Model to host state
 */
class TimerViewModel (
    private val dao: TimerDao
): ViewModel() {

    private val _filterType = MutableStateFlow(FilterType.ALL)

    // reactive change (button changes data source (query))
    private val _timers = _filterType
        .flatMapLatest { filterType ->
            when(filterType) {
                FilterType.ALL -> dao.getTimers()
                FilterType.STANDARD -> dao.getStandardTimers()
                FilterType.INTERVAL -> dao.getIntervalTimers()
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
            is TimerEvent.setTimerName -> {
                _state.update { it.copy(
                    timerName = event.timerName
                ) }
            }
            is TimerEvent.setIsInterval -> {
                _state.update { it.copy(
                    isInterval = event.isInterval
                ) }
            }
            is TimerEvent.setNumIntervals -> {
                _state.update { it.copy(
                    numIntervals = event.numIntervals
                ) }
            }
            is TimerEvent.setFlowMusicDuration -> {
                _state.update { it.copy(
                    flowMusicDuration = event.flowMusicDuration
                ) }
            }
            is TimerEvent.setBreakMusicDuration -> {
                _state.update { it.copy(
                    breakMusicDuration = event.breakMusicDuration
                ) }
            }
            is TimerEvent.setIsSaved -> {
                _state.update { it.copy(
                    isSaved = event.isSaved
                ) }
            }
            is TimerEvent.deleteTimer -> {
                viewModelScope.launch {
                    dao.deleteTimer(event.timer)
                }
            }
            is TimerEvent.SaveTimer -> {
                // set local variables
                val timerName = state.value.timerName
                val isInterval = state.value.isInterval
                val numIntervals = state.value.numIntervals
                val flowMusicDuration = state.value.flowMusicDuration
                val breakMusicDuration = state.value.breakMusicDuration
                val isSaved = state.value.isSaved

                // (for now) field specification checking logic upon saving
                if (timerName.isBlank() || flowMusicDuration.equals(0) || flowMusicDuration.isNegative()
                    || breakMusicDuration.equals(0) || breakMusicDuration.isNegative()
                    || numIntervals <= 0) {
                    return
                }

                // create contact and insert into database
                val newTimer = Timer(
                    timerName = timerName,
                    isInterval = isInterval,
                    numIntervals = numIntervals,
                    flowMusicDuration = flowMusicDuration,
                    breakMusicDuration = breakMusicDuration,
                    isSaved = isSaved
                )

                viewModelScope.launch {
                    dao.upsertTimer(newTimer)
                }

                // update state (reset to defaults)
                _state.update { it.copy(
                    isAddingTimer = false,

                    timerName = "",
                    isInterval = false,
                    numIntervals = 1,
                    flowMusicDuration = 0.minutes,
                    breakMusicDuration = 0.minutes,
                    isSaved = false
                ) }
            }
            is TimerEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingTimer = false
                ) }
            }
            is TimerEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingTimer = true
                ) }
            }
            is TimerEvent.filterTimers -> {
                // triggers change to map correct type of timers to our _timers Flow list
                _filterType.value = event.filterType
            }
        }
    }
}