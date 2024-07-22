package com.example.tunetide.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.MusicType
import com.example.tunetide.database.Playback
import com.example.tunetide.database.StateType
import com.example.tunetide.mp3player.MP3PlayerManager
import com.example.tunetide.repository.MP3Repository
import com.example.tunetide.repository.PlaybackRepository
import com.example.tunetide.repository.SpotifyRepository
import com.example.tunetide.spotify.SpotifyController
import com.example.tunetide.ui.TuneTideApp
import com.example.tunetide.ui.timer.TimerUIState
import com.example.tunetide.ui.timer.toTimerDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * View Model to host state of the timer that is running
 */

interface IHomePageViewModel {
    suspend fun homeScreenTimer()
    fun timeFormat(timeSec: Long) : String
    fun play()
    fun pause()
    fun skipSong()
    suspend fun getStartingTimerValue()
    fun getStartingMusic()
    fun startNextInterval()
    fun finish()
    fun restart()
    fun onCleared()
    fun toggleShuffle()
    fun isShuffling() : Boolean
}
class HomePageViewModel (
    context: Context,
    private val playbackRepository: PlaybackRepository,
    private val mP3Repository: MP3Repository,
    private val spotifyRepository: SpotifyRepository
): ViewModel(), IHomePageViewModel {

    private var _timerId: Int = -1
        private set

    private var mp3PlayerManager: MP3PlayerManager = MP3PlayerManager(context)
    private var spotifyController: SpotifyController = SpotifyController(context)

    private var finishedFlag: Boolean = false

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

    private val _currentSongName = MutableStateFlow<String>("Not Playing")
    val currentSongName: StateFlow<String> = _currentSongName

    private val _currentPlaylistName = MutableStateFlow("No Playlist")
    val currentPlaylistName: StateFlow<String> = _currentPlaylistName

    override suspend fun homeScreenTimer() {
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
            observeCurrentSong()
        }
    }

    private fun observeCurrentSong() {
        if (_timerId == -1) {
        } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
            CoroutineScope(Dispatchers.IO).launch {
                mp3PlayerManager.currentSongName.collect { songName ->
                    _currentSongName.value = songName
                }
            }
        } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
            CoroutineScope(Dispatchers.Main).launch {
                _currentSongName.value = spotifyController.getCurrentTrackName()
            }
        }
    }

    private fun observeCurrentPlaylist(playlistId: Int) {
        if (_timerId == -1) {
        } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
            CoroutineScope(Dispatchers.IO).launch {
                mP3Repository.getMP3PlaylistById(playlistId).collect { playlist ->
                    if (playlist != null) {
                        _currentPlaylistName.value = playlist.playlistName
                    }
                }
            }
        } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY)
        {
            CoroutineScope(Dispatchers.IO).launch {
                spotifyRepository.getSpotifyPlaylistById(playlistId).collect { playlist ->
                    if (playlist != null) {
                        spotifyController.setPlaylistUrl(playlist.uriPath)
                        if (playlist.playlistName != null) {
                            _currentPlaylistName.value = playlist.playlistName
                        }
                    }
                }
            }
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
                spotifyController.play()
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
                spotifyController.pause()
            }
        }
    }

    override fun skipSong() {
        CoroutineScope(Dispatchers.IO).launch {
            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                mp3PlayerManager.skipToNextSong()

            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                spotifyController.skipToNextSong()
            }
        }
    }

    override fun toggleShuffle() {
        spotifyController.toggleShuffle()
    }

    override fun isShuffling(): Boolean {
        return spotifyController.isShuffling
    }

    override suspend fun getStartingTimerValue() {
        val state = playbackRepository.getStateType()
        _currentTimerVal.value = when (state) {
            StateType.FLOW -> playbackRepository.getFlowDurationSeconds()
            StateType.BREAK -> playbackRepository.getBreakDurationSeconds()
            else -> 0
        }
    }

    override fun getStartingMusic() {
        CoroutineScope(Dispatchers.IO).launch {
            if (playbackRepository.getPlayingMusicSource() == MusicType.MP3) {
                val playlistId = playbackRepository.getPlayingMusicPlaylistId()
                observeCurrentPlaylist(playlistId)
                val playlist = mP3Repository.getMP3FileByPlaylist(playlistId)
                mp3PlayerManager.switchPlaylist(playlist)
            } else if (playbackRepository.getPlayingMusicSource() == MusicType.SPOTIFY) {
                val playlistId = playbackRepository.getPlayingMusicPlaylistId()
                observeCurrentPlaylist(playlistId)
                spotifyRepository.getSpotifyPlaylistById(playlistId).collect {playlist ->
                    if (playlist != null) {
                        spotifyController.setPlaylistUrl(playlist.uriPath)
                    }
                }

            }
        }
    }

    override fun startNextInterval() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("HomePageTimer", "calling start next interval")
            // if last interval finishes, finish

            if (finishedFlag) {
                finish()

                finishedFlag = false
            } else {
                if (playbackRepository.getCurrentInterval() == playbackRepository.getPlayingTimerNumIntervals()) {
                    //LAST INTERVAL
                    playbackRepository.startNextInterval()
                    getStartingTimerValue()
                    currentTimerVal.first { it == _currentTimerVal.value }
                    getStartingMusic()
                    homeScreenTimer()

                    finishedFlag = true
                    //play one more interval??
                } else {
                    playbackRepository.startNextInterval()
                    getStartingTimerValue()
                    currentTimerVal.first { it == _currentTimerVal.value }
                    getStartingMusic()
                    homeScreenTimer()
                }
            }
        }
    }

    // same as cancelling a timer
    override fun finish() {
        CoroutineScope(Dispatchers.IO).launch {
            if (_timerId == -1) {
            } else if (playbackRepository.getPlayingMusicSource()  == MusicType.MP3) {
                mp3PlayerManager.stopMusic()
            } else if (playbackRepository.getPlayingMusicSource()  == MusicType.SPOTIFY) {
                spotifyController.pause()
            }
            Log.d("HomePageTimer", "inside finish")
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
            // TODO is below how we want to restart??
            getStartingMusic()
            play()
        }
    }

    override fun onCleared() {
        mp3PlayerManager.releaseMediaPlayer()
        spotifyController.disconnect()
        super.onCleared()
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
