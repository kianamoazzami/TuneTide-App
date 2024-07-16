package com.example.tunetide.ui.home

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

/**
 * View Model to host state of the timer that is running
 */
class HomePageViewModel (
    private val playbackRepository: PlaybackRepository
): ViewModel() {

    private var _timerId: Int = -1
        private set

    val playbackUIState: StateFlow<PlaybackUIState> = playbackRepository.getPlayback()
        .filterNotNull()
        .map {
            PlaybackUIState(playbackDetails = it.toPlaybackDetails())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PlaybackUIState()
        )

    val timerUIState: StateFlow<TimerUIState> = playbackRepository.getPlayingTimer()
        .filterNotNull()
        .map {
            TimerUIState(timerDetails = it.toTimerDetails())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = TimerUIState()
        )

    private val _currentTimerVal = MutableStateFlow(0)
    val currentTimerVal: StateFlow<Int> = _currentTimerVal

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun homeScreenTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            while (isPlaying.value && currentTimerVal.value > 0) {
                delay(1000)
                _currentTimerVal.value -= 1
            }
            if (isPlaying.value && currentTimerVal.value <= 0) {
                startNextInterval()
            }
        }
    }

    fun timeFormat(timeSec: Long): String {
        val hours = (timeSec) / 3600
        val minutes = (timeSec % 3600) / 60
        val seconds = (timeSec) % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _timerId = playbackRepository.getPlayingTimerId()
            getStartingTimerValue()
        }
    }

    fun play() {
        CoroutineScope(Dispatchers.IO).launch {
            playbackRepository.play()
            _isPlaying.value = true
            homeScreenTimer()

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
        CoroutineScope(Dispatchers.IO).launch {
            playbackRepository.pause()
            _isPlaying.value = false

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
        CoroutineScope(Dispatchers.IO).launch {
            var playlistId: Int = playbackRepository.getPlayingMusicPlaylistId()

            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                // TODO @KIANA

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                // TODO @ERICA
            }
        }
    }

    fun getStartingTimerValue() {
        CoroutineScope(Dispatchers.IO).launch {
            if (playbackRepository.getStateType() == StateType.FLOW) {
                _currentTimerVal.value = playbackRepository.getFlowDurationSeconds()
            } else if (playbackRepository.getStateType() == StateType.BREAK) {
                _currentTimerVal.value = playbackRepository.getBreakDurationSeconds()
            } else {
                _currentTimerVal.value = 0
            }
        }
    }

    fun startNextInterval() {
        CoroutineScope(Dispatchers.IO).launch {
            // if last interval finishes, finish
            if (playbackRepository.getCurrentInterval() == playbackRepository.getPlayingTimerNumIntervals() &&
                playbackRepository.getStateType() == StateType.BREAK
            ) {
                finish()
            } else {
                playbackRepository.startNextInterval()
                getStartingTimerValue()
                homeScreenTimer()
            }
        }
    }

    // same as cancelling a timer
    fun finish() {
        CoroutineScope(Dispatchers.IO).launch {
            // TODO @ERICA @KIANA stop music
            playbackRepository.invalidatePlayback()
            _isPlaying.value = false
            _currentTimerVal.value = 0
        }
    }

    fun restart() {
        CoroutineScope(Dispatchers.IO).launch {
            playbackRepository.restartTimer()
            _isPlaying.value = true
            homeScreenTimer()
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
    val playbackDetails: PlaybackDetails = PlaybackDetails()
)

/**
 * represents the "UI" for playback
 */
// Updated to match invalid timer details in PlaybackDao's invalidatePlayback()
data class PlaybackDetails(
    val id: Int = -1,
    val timerId: Int = -1,
    val stateType: Int = 2,
    val currentInterval: Int = -1,
    val currentIntervalRemainingSeconds: Int = 0,
    val isPlaying: Boolean = false
)

/**
 * conversion of classes
 */
fun PlaybackDetails.toPlayback(): Playback = Playback (
    id = id,
    timerId = timerId,
    stateType = stateType,
    currentInterval = currentInterval,
    currentIntervalRemainingSeconds = currentIntervalRemainingSeconds,
    isPlaying = isPlaying

)

fun Playback.toPlaybackDetails(): PlaybackDetails = PlaybackDetails (
    id = id,
    timerId = timerId,
    stateType = stateType,
    currentInterval = currentInterval,
    currentIntervalRemainingSeconds = currentIntervalRemainingSeconds,
    isPlaying = isPlaying
)

fun Playback.toPlaybackUIState(): PlaybackUIState = PlaybackUIState(
    playbackDetails = this.toPlaybackDetails()
)
