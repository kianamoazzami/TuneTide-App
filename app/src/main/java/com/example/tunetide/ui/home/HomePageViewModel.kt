package com.example.tunetide.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.MusicType
import com.example.tunetide.database.Playback
import com.example.tunetide.database.StateType
import com.example.tunetide.database.Timer
import com.example.tunetide.repository.PlaybackRepository
import com.example.tunetide.repository.TimerRepository
import com.example.tunetide.ui.timer.TimerDetails
import com.example.tunetide.ui.timer.TimerUIState
import com.example.tunetide.ui.timer.toTimerDetails
import com.example.tunetide.ui.timer.toTimerUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

// TODO @MIA should have a way to halt the queue / standards
/**
 * View Model to host state of the timer that is running
 */
class HomePageViewModel (
    private val playbackRepository: PlaybackRepository
): ViewModel() {

    // TEMP IN -> RUNTIME ERROR
    private val _timerId = 1
    // TEMP OUT -> RUNTIME ERROR
    //private val _timerId = playbackRepository.getPlayingTimerId()

    val _playbackUIState = MutableStateFlow(PlaybackUIState())
    val playbackUIState: StateFlow<PlaybackUIState> = _playbackUIState.asStateFlow()

    val _timerUIState = MutableStateFlow(TimerUIState())
    val timerUIState: StateFlow<TimerUIState> = _timerUIState.asStateFlow()

    fun timeFormat(timeMillis: Long): String {
        val minutes = (timeMillis / 1000) / 60
        val seconds = (timeMillis / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    fun setCurrentTime(newVal: Int) {
        _playbackUIState.update { currentState ->
            currentState.copy(
                currentIntervalRemainingSeconds = newVal
            )
        }
    }

    fun changePlayingStatus(newStatus: Boolean) {
        if (newStatus == true) {

            _playbackUIState.update { currentState ->
                currentState.copy(
                    isPlaying = true
                )
            }

        }
        else {

            _playbackUIState.update { currentState ->
                currentState.copy(
                    isPlaying = false
                )
            }

        }

    }

    // TODO @MIA pause and play different functions?
    // TODO @MIA @ERICA @KIANA I want the backend play and pause booleans / countdowns to be
    //      synchronous with the music players
    fun play() {
        viewModelScope.launch {
            playbackRepository.play()

            var playlistId: Int = playbackRepository.getPlayingMusicPlaylistId()

            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                // TODO @KIANA

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                // TODO @ERICA
            }
        }
    }

    fun pause() {
        viewModelScope.launch {
            playbackRepository.pause()

            var playlistId: Int = playbackRepository.getPlayingMusicPlaylistId()

            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                // TODO @KIANA

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                // TODO @ERICA
            }
        }
    }

    fun skipSong() {
        viewModelScope.launch {
            var playlistId: Int = playbackRepository.getPlayingMusicPlaylistId()

            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                // TODO @KIANA

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                // TODO @ERICA
            }
        }
    }

    fun getStartingTimerValue(): Int {
        if (playbackRepository.getStateType() == StateType.FLOW) {
            return playbackRepository.getFlowDurationSeconds()
        } else if (playbackRepository.getStateType() == StateType.BREAK) {
            return playbackRepository.getBreakDurationSeconds()
        } else {
            return 0
        }
    }

    fun startNextInterval() {
        viewModelScope.launch {
            // if last interval finishes, finish
            if (playbackRepository.getCurrentInterval() == playbackRepository.getPlayingTimerNumIntervals() &&
                playbackRepository.getStateType() == StateType.BREAK
            ) {
                finish()
            } else {
                playbackRepository.startNextInterval()
            }
        }
    }

    // same as cancelling a timer
    fun finish() {
        viewModelScope.launch {
            // TODO @ERICA @KIANA stop music
            playbackRepository.invalidatePlayback()
            // TODO @MIA invoke next in queue??? (decide how queue will work)
        }
    }

    fun restart() {
        viewModelScope.launch {
            playbackRepository.restartTimer()
            // TODO @ERICA @KIANA restart music
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

// TODO @ERICA @KIANA somehow get song title, image, etc here based on logic of
//      whether or not spotify or mp3 music is selected / playing
//      suggestion would be to create a MusicUIState (like playbackUIState and timerUIState)
//      this would make it easy to access on home screen

/**
 * represents "UI state" for playback
 */
data class PlaybackUIState(
    var id: Int = -1,
    var timerId: Int = -1,
    var stateType: Int = 2,
    var currentInterval: Int = 0,
    var currentIntervalRemainingSeconds: Int = 0,
    var isPlaying: Boolean = false
)

fun Playback.toPlaybackUIState(): PlaybackUIState = PlaybackUIState(
    id = id,
    timerId = timerId,
    stateType = stateType,
    currentInterval = currentInterval,
    currentIntervalRemainingSeconds = currentIntervalRemainingSeconds,
    isPlaying = isPlaying
)

fun PlaybackUIState.toPlayback(): Playback = Playback(
    id = id,
    timerId = timerId,
    stateType = stateType,
    currentInterval = currentInterval,
    currentIntervalRemainingSeconds = currentIntervalRemainingSeconds,
    isPlaying = isPlaying
)

