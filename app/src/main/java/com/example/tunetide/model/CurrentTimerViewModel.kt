package com.example.tunetide.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.FilterType
import com.example.tunetide.database.Timer
import com.example.tunetide.event.CurrentTimerEvent
import com.example.tunetide.event.TimerEvent
import com.example.tunetide.repository.TimerDao
import com.example.tunetide.repository.TimerRepository
import com.example.tunetide.state.TimerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

/**
 * View Model to host state
 */
class CurrentTimerViewModel (
    private val timerRepository: TimerRepository
): ViewModel() {

    fun onEvent(event: CurrentTimerEvent) {
        when(event) {
            is CurrentTimerEvent.PlayTimer -> {
                // TODO call music player (pass timerId)
            }
            is CurrentTimerEvent.PauseTimer -> {
                // TODO call music player (pass timerId)
            }
            is CurrentTimerEvent.SkipSong -> {
                // TODO call music player (pass timerId)
            }
            is CurrentTimerEvent.CompleteInterval -> {
                // TODO get timer by id to see if timer is done (break of last interval)
                // if (timer is done) isFinished = true
                // else { }
                //  TODO
                //      - if state = "flow" then "break"
                //      - increment interval number
            }
        }
    }
}