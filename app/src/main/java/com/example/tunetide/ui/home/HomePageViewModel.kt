package com.example.tunetide.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.MusicType
import com.example.tunetide.database.Playback
import com.example.tunetide.database.StateType
import com.example.tunetide.mp3player.MP3PlayerManager
import com.example.tunetide.repository.MP3Repository
import com.example.tunetide.repository.PlaybackRepository
import com.example.tunetide.ui.timer.TimerUIState
import com.example.tunetide.ui.timer.toTimerDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * View Model to host state of the timer that is running
 */

interface IHomePageViewModel {
    fun homeScreenTimer()
    fun timeFormat(timeSec: Long) : String
    fun play()
    fun pause()
    fun skipSong()
    fun getStartingTimerValue()
    fun getStartingMusic()
    fun startNextInterval()
    fun finish()
    fun restart()
    fun onCleared()
}
class HomePageViewModel (
    context: Context,
    private val playbackRepository: PlaybackRepository,
    private val mP3Repository: MP3Repository
): ViewModel(), IHomePageViewModel {

    private var _timerId: Int = -1
        private set

    private var mp3PlayerManager: MP3PlayerManager = MP3PlayerManager(context)

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

    override fun homeScreenTimer() {
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

    override fun timeFormat(timeSec: Long): String {
        val hours = (timeSec) / 3600
        val minutes = (timeSec % 3600) / 60
        val seconds = (timeSec) % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _timerId = playbackRepository.getPlayingTimerId()
            getStartingTimerValue()
            getStartingMusic()
        }
    }

    override fun play() {
        CoroutineScope(Dispatchers.IO).launch {
            playbackRepository.play()
            _isPlaying.value = true
            homeScreenTimer()

            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                mp3PlayerManager.play()

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                // TODO @ERICA
            }
        }
    }

    override fun pause() {
        CoroutineScope(Dispatchers.IO).launch {
            playbackRepository.pause()
            _isPlaying.value = false

            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                mp3PlayerManager.pause()

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                // TODO @ERICA
            }
        }
    }

    override fun skipSong() {
        CoroutineScope(Dispatchers.IO).launch {
            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                mp3PlayerManager.skipToNextSong()

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                // TODO @ERICA
            }
        }
    }

    override fun getStartingTimerValue() {
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

    override fun getStartingMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                val playlistId = playbackRepository.getPlayingMusicPlaylistId()
                val playlist = mP3Repository.getMP3FileByPlaylist(playlistId)
                mp3PlayerManager.switchPlaylist(playlist)
            }
        }
    }

    override fun startNextInterval() {
        CoroutineScope(Dispatchers.IO).launch {
            // if last interval finishes, finish
            if (playbackRepository.getCurrentInterval() == playbackRepository.getPlayingTimerNumIntervals() &&
                playbackRepository.getStateType() == StateType.BREAK
            ) {
                finish()
            } else {
                playbackRepository.startNextInterval()
                getStartingTimerValue()
                getStartingMusic()
                homeScreenTimer()
            }
        }
    }

    // same as cancelling a timer
    override fun finish() {
        CoroutineScope(Dispatchers.IO).launch {
            // TODO @ERICA stop music
            mp3PlayerManager.stopMusic()
            playbackRepository.invalidatePlayback()
            _isPlaying.value = false
            _currentTimerVal.value = 0
        }
    }

    override fun restart() {
        CoroutineScope(Dispatchers.IO).launch {
            playbackRepository.restartTimer()
            _isPlaying.value = true
            homeScreenTimer()
            // TODO @ERICA @KIANA restart music
        }
    }

    override fun onCleared() {
        super.onCleared()
        mp3PlayerManager.releaseMediaPlayer()
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
