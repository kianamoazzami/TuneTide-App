package com.example.tunetide.ui.mp3

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.example.tunetide.database.MP3Playlist
import com.example.tunetide.repository.MP3Repository

class MP3PlaylistEntryViewModel (
    mp3Repository: MP3Repository
): ViewModel() {

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