package com.example.tunetide.ui.mp3

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunetide.database.MP3File
import com.example.tunetide.repository.MP3Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class MP3PlaylistDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mp3Repository: MP3Repository,
) : ViewModel() {

    private val mp3PlaylistId: Int = checkNotNull(savedStateHandle[MP3PlaylistDetailsDestination.mp3PlaylistIdArg])

    private val playlistFlow = mp3Repository.getMP3PlaylistById(mp3PlaylistId).filterNotNull()

    private val mp3FilesFlow = mp3Repository.getMP3FileByPlaylist(mp3PlaylistId)

    val uiState: StateFlow<MP3PlaylistDetailsUIState> =
        playlistFlow.combine(mp3FilesFlow) { playlist, mp3Files ->
            MP3PlaylistDetailsUIState(
                mp3PlaylistDetails = playlist.toMP3PlaylistDetails(),
                mp3Files = mp3Files
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = MP3PlaylistDetailsUIState()
        )

    suspend fun deleteMP3Playlist() {
        mp3Repository.deleteFilesByPlaylistId(uiState.value.mp3PlaylistDetails.toMP3Playlist().playlistId)
        mp3Repository.deletePlaylist(uiState.value.mp3PlaylistDetails.toMP3Playlist())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MP3PlaylistDetailsUIState(
    val mp3PlaylistDetails: MP3PlaylistDetails = MP3PlaylistDetails(),
    val mp3Files: List<MP3File> = emptyList()
)
