package com.example.tunetide.ui.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.repository.SpotifyRepository
import com.example.tunetide.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerEditViewModel (
    private val savedStateHandle: SavedStateHandle,
    private val timerRepository: TimerRepository,
    val spotifyRepository: SpotifyRepository
): ViewModel() {

    var timerUIState by mutableStateOf(TimerUIState())
        private set

    private val timerId: Int = checkNotNull(savedStateHandle[TimerEditDestination.timerIdArg])

    init {
        CoroutineScope(Dispatchers.IO).launch {
            timerUIState = timerRepository.getTimerById(timerId)
                .filterNotNull()
                .first()
                .toTimerUIState(true)
        }
    }

    // updates item UI
    fun updateUIState(timerDetails: TimerDetails) {
        timerUIState = TimerUIState(timerDetails = timerDetails, isValidEntry = validateInput(timerDetails))
    }

    suspend fun updateTimer() {
        if (validateInput()) {
            timerRepository.updateTimer(timerUIState.timerDetails.toTimer())
        }
    }

    // NOTE: UI configuration will prevent some invalid inputs
    private fun validateInput(uiState: TimerDetails = timerUIState.timerDetails): Boolean {
        return with(uiState) {
            timerName.isNotBlank() &&
                    numIntervals > 0 &&
                    (spotifyFlowMusicPlaylistId != -1 || mp3FlowMusicPlaylistId != -1) &&
                    (spotifyBreakMusicPlaylistId != -1 || mp3BreakMusicPlaylistId != -1) &&
                    flowMusicDurationSeconds > 0 &&
                    breakMusicDurationSeconds > 0
        }
    }
}