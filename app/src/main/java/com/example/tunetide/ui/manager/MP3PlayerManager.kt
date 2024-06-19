package com.example.tunetide.ui.manager

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.*
import com.example.tunetide.R

class MP3PlayerManager (var context: Context){
    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrack = R.raw.flowmusic
    var isPlaying by mutableStateOf(false)
        private set

    init {
        mediaPlayer = MediaPlayer.create(context, currentTrack)
    }

    fun togglePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        isPlaying = !isPlaying
    }

    fun switchTrack(trackResId: Int) {
        currentTrack = trackResId
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(context, currentTrack)
        if (isPlaying) {
            mediaPlayer.start()
        }
    }

    fun onDestroy() {
        mediaPlayer.release();
    }

}