package com.example.tunetide.ui.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.SpotifyPlaylist
import com.example.tunetide.repository.SpotifyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SpotifyPlaylistsViewModel (
    spotifyRepository: SpotifyRepository
): ViewModel() {

    val spotifyPlaylistsUIState: StateFlow<SpotifyPlaylistsUIState> =
        spotifyRepository.getSpotifyPlaylists().map { SpotifyPlaylistsUIState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SpotifyPlaylistsUIState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

/**
 *  UI state for the list of timers
 */
data class SpotifyPlaylistsUIState(
    val spotifyPlaylists: List<SpotifyPlaylist> = listOf()
)