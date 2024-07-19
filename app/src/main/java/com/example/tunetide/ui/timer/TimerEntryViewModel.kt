package com.example.tunetide.ui.timer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tunetide.database.SpotifyPlaylist
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.Timer
import com.example.tunetide.repository.TimerRepository
import com.example.tunetide.ui.mp3.toMP3Playlist
import kotlinx.coroutines.flow.toList
import com.example.tunetide.repository.SpotifyRepository
import kotlinx.coroutines.flow.map
import com.example.tunetide.repository.ITimerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerEntryViewModel(
    private val timerRepository: ITimerRepository
) : ViewModel() {
/**
 * View Model to host state
 */
class TimerEntryViewModel (
    private val timerRepository: TimerRepository,
    val spotifyRepository : SpotifyRepository
): ViewModel() {

    // holds current timer UI state
    var timerUIState by mutableStateOf(TimerUIState())
        private set

    val timers: StateFlow<List<Timer>> = timerRepository.getTimers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // updates item UI
    fun updateUIState(timerDetails: TimerDetails) {
        timerUIState = TimerUIState(timerDetails = timerDetails, isValidEntry = validateInput(timerDetails))
    }

    suspend fun insertTimer() {
        if (timerUIState.isValidEntry) {
            updateUIState(timerUIState.timerDetails.copy(isSaved = true))
           timerRepository.insertTimer(timerUIState.timerDetails.toTimer())
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

/**
 * represents "UI state" for timer
 */
data class TimerUIState(
    val timerDetails: TimerDetails = TimerDetails(),
    val isValidEntry: Boolean = false
)

/**
 * represents the "UI" for timer
 */
data class TimerDetails(
    val timerId: Int = 0,
    val timerName: String = "",
    val isInterval: Boolean = false,
    val numIntervals: Int = 1,
    val spotifyFlowMusicPlaylistId: Int = -1,
    val mp3FlowMusicPlaylistId: Int = -1,
    val flowMusicDurationSeconds: Int = 0,
    val spotifyBreakMusicPlaylistId: Int = -1,
    val mp3BreakMusicPlaylistId: Int = -1,
    val breakMusicDurationSeconds: Int = 0,
    val isSaved: Boolean = false,
)

/**
 * conversion of classes
 */
// TODO @MIA for now there is no type conversion, can decide further later if any other mods needed
fun TimerDetails.toTimer(): Timer = Timer(
    timerId = timerId,
    timerName = timerName,
    isInterval = isInterval,
    numIntervals = numIntervals.toInt() ?: 1,
    spotifyFlowMusicPlaylistId = spotifyFlowMusicPlaylistId.toInt() ?: -1,
    mp3FlowMusicPlaylistId = mp3FlowMusicPlaylistId.toInt() ?: -1,
    flowMusicDurationSeconds = flowMusicDurationSeconds.toInt() ?: 1, //
    spotifyBreakMusicPlaylistId = spotifyBreakMusicPlaylistId.toInt() ?: -1,
    mp3BreakMusicPlaylistId = mp3BreakMusicPlaylistId.toInt() ?: -1,
    breakMusicDurationSeconds = breakMusicDurationSeconds.toInt() ?: 1,
    isSaved = isSaved
)

fun Timer.toTimerDetails(): TimerDetails = TimerDetails(
    timerId = timerId ?: -1,
    timerName = timerName ?: "Unnamed",
    isInterval = isInterval,
    numIntervals = numIntervals.toInt() ?: 1,
    spotifyFlowMusicPlaylistId = spotifyFlowMusicPlaylistId.toInt() ?: -1,
    mp3FlowMusicPlaylistId = mp3FlowMusicPlaylistId.toInt() ?: -1,
    flowMusicDurationSeconds = flowMusicDurationSeconds.toInt() ?: 1,
    spotifyBreakMusicPlaylistId = spotifyBreakMusicPlaylistId.toInt() ?: -1,
    mp3BreakMusicPlaylistId = mp3BreakMusicPlaylistId.toInt() ?: -1,
    breakMusicDurationSeconds = breakMusicDurationSeconds.toInt() ?: 1,
    isSaved = isSaved
)

fun Timer.toTimerUIState(isValidEntry: Boolean = false): TimerUIState = TimerUIState(
    timerDetails = this.toTimerDetails(),
    isValidEntry = isValidEntry
)