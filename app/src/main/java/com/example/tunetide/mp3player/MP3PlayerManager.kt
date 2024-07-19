package com.example.tunetide.mp3player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.tunetide.database.MP3File
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

class MP3PlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var playlist: List<MP3File> = emptyList()
    private var currentSongIndex: Int = 0
    private var isPlaying by mutableStateOf(false)
    private var playlistJob: Job? = null

    init {
        mediaPlayer.setOnPreparedListener {
            if (isPlaying) {
                mediaPlayer.start()
            }
        }
        mediaPlayer.setOnCompletionListener {
            skipToNextSong()
        }
    }

    fun play() {
        if (playlist.isEmpty()) {
            return
        }
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            isPlaying = true
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
        }
    }

    private fun playCurrentSong() {
        if (playlist.isNotEmpty() && currentSongIndex < playlist.size) {
            val currentSong = playlist[currentSongIndex].filePath
            mediaPlayer.reset()

            val uri = Uri.parse(currentSong)
            mediaPlayer.setDataSource(context, uri)

            mediaPlayer.prepareAsync()
        }
    }

    fun skipToNextSong() {
        currentSongIndex++
        if (currentSongIndex >= playlist.size) {
            currentSongIndex = 0 //restart playlist
            playCurrentSong()
        } else {
            playCurrentSong()
        }
    }

    fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        isPlaying = false
    }

    fun switchPlaylist(newPlaylistFlow: Flow<List<MP3File>>) {
        playlistJob?.cancel()
        playlistJob = CoroutineScope(Dispatchers.IO).launch {
            newPlaylistFlow.collectLatest { newPlaylist ->
                withContext(Dispatchers.Main) {
                    playlist = newPlaylist
                    currentSongIndex = 0
                    playCurrentSong()
                }
            }
        }
    }

    fun releaseMediaPlayer() {
        isPlaying = false;
        mediaPlayer.release()
    }
}
