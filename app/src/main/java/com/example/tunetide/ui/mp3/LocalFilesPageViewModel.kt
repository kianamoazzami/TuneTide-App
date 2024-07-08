package com.example.tunetide.ui.mp3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.repository.MP3Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LocalFilesPageViewModel (
    mp3Repository: MP3Repository
): ViewModel() {

    val localFilesUIState: StateFlow<LocalFilesUIState> =
        mp3Repository.getMP3Playlists().map { LocalFilesUIState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = LocalFilesUIState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

/**
 *  UI state for the list of timers
 */
data class LocalFilesUIState(
    val mp3Playlists: List<MP3Playlist> = listOf()
)