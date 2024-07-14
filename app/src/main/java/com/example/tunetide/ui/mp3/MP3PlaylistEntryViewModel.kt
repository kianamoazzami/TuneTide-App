package com.example.tunetide.ui.mp3

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tunetide.database.MP3File
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.repository.MP3Repository

class MP3PlaylistEntryViewModel (
    private val mp3Repository: MP3Repository
): ViewModel() {

    var mp3PlaylistUIState by mutableStateOf(MP3PlaylistUIState())
        private set

    var mp3FilesUIState by mutableStateOf(MP3FilesUIState())
        private set

    fun updateUIState(mp3PlaylistDetails: MP3PlaylistDetails) {
        mp3PlaylistUIState = MP3PlaylistUIState(
            mp3PlaylistDetails = mp3PlaylistDetails,
            isEntryValid = validateInput(mp3PlaylistDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            mp3Repository.insertPlaylist(mp3PlaylistUIState.mp3PlaylistDetails.toMP3Playlist())

            //first do playlist ^ to save a playlist ID then use that Id to assign to files to do files
        }
    }

    private fun validateInput(uiState: MP3PlaylistDetails = mp3PlaylistUIState.mp3PlaylistDetails): Boolean {
        return with(uiState) {
            playlistName.isNotBlank()
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

//FILES
data class MP3FilesUIState(
    val mp3FileDetailsList: MutableList<MP3FileDetails> = mutableListOf(), //mutable list? or list?
    val isEntryValid: Boolean = false //at least one song selected
)

data class MP3FileDetails(
    val fileId: Int = 0,
    val playlistId: Int = 0,
    val fileName: String? = "",
    val filePath: String = "",
    val fileCover: String? = ""
)

fun MP3FileDetails.toMP3File(): MP3File = MP3File(
    fileId = fileId,
    playlistId = playlistId,
    fileName = fileName,
    filePath = filePath,
    fileCover = fileCover
)