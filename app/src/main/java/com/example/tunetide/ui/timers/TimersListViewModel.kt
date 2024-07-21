package com.example.tunetide.ui.timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.FilterType
import com.example.tunetide.database.Playback
import com.example.tunetide.database.StateType
import com.example.tunetide.database.Timer
import com.example.tunetide.repository.PlaybackRepository
import com.example.tunetide.repository.TimerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.filterNotNull

class TimersListViewModel (
    private val timerRepository: TimerRepository,
    private val playbackRepository: PlaybackRepository
): ViewModel() {

    private val _filterType = MutableStateFlow(FilterType.ALL)

    private val _timers: StateFlow<List<Timer>> = _filterType
        .flatMapLatest { filterType ->
            when(filterType) {
                FilterType.ALL -> timerRepository.getTimers().filterNotNull()
                FilterType.STANDARD -> timerRepository.getStandardTimers().filterNotNull()
                FilterType.INTERVAL -> timerRepository.getIntervalTimers().filterNotNull()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _timerListUIState = MutableStateFlow(TimerListUIState())

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
        _filterType.value = filterType
    }

    suspend fun setPlayback(timer: Timer) {
        val playback = Playback(
            id = 1,
            timerId = timer.timerId,
            stateType = StateType.FLOW.value, //start with flow state
            currentInterval = 1,
            currentIntervalRemainingSeconds = timer.flowMusicDurationSeconds,
            isPlaying = false //turns to true when played
        )

        playbackRepository.setPlayback(playback)

    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TimerListUIState(
    val timers: List<Timer> = listOf(),
    val filterType: FilterType = FilterType.ALL
)
