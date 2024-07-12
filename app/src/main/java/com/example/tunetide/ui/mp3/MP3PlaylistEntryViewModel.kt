package com.example.tunetide.ui.mp3

import androidx.annotation.NonNull
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.repository.MP3Repository

class MP3PlaylistEntryViewModel (
    private val mp3Repository: MP3Repository
): ViewModel() {

    var mp3PlaylistUIState by mutableStateOf(MP3PlaylistUIState())
        private set

    fun updateUIState(mp3PlaylistDetails: MP3PlaylistDetails) {
        mp3PlaylistUIState =
            MP3PlaylistUIState(mp3PlaylistDetails = mp3PlaylistDetails, isEntryValid = validateInput(mp3PlaylistDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            mp3Repository.insertPlaylist(mp3PlaylistUIState.mp3PlaylistDetails.toMP3Playlist())
        }
    }

    private fun validateInput(uiState: MP3PlaylistDetails = mp3PlaylistUIState.mp3PlaylistDetails): Boolean {
        return with(uiState) {
            playlistName.isNotBlank() // && there is at least one song added!
        }
    }
}

data class MP3PlaylistUIState(
    val mp3PlaylistDetails: MP3PlaylistDetails = MP3PlaylistDetails(),
    val isEntryValid: Boolean = false
)

data class MP3PlaylistDetails(
    val playlistId: Int = 0,
    val playlistName: String = "",
    val uriPath: String = "",
    val playlistCover: String? = "",
    val isSaved: Boolean = true
)

fun MP3PlaylistDetails.toMP3Playlist(): MP3Playlist = MP3Playlist(
    playlistId = playlistId,
    playlistName = playlistName,
    uriPath = uriPath,
    playlistCover = playlistCover,
    isSaved = isSaved
)

fun MP3Playlist.toMP3PlaylistUIState(isEntryValid: Boolean = false): MP3PlaylistUIState = MP3PlaylistUIState(
    mp3PlaylistDetails = this.toMP3PlaylistDetails(),
    isEntryValid = isEntryValid
)

fun MP3Playlist.toMP3PlaylistDetails(): MP3PlaylistDetails = MP3PlaylistDetails(
    playlistId = playlistId,
    playlistName = playlistName,
    uriPath = uriPath,
    playlistCover = playlistCover,
    isSaved = isSaved
)