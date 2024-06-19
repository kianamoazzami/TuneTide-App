package com.example.tunetide.ui.manager

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.*
import com.example.tunetide.R

class MP3PlayerManager(var context: Context) {
    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrack = R.raw.flowmusic // resID
    var isPlaying by mutableStateOf(false)
        private set

    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener {
            if (isPlaying) {
                mediaPlayer.start()
            }
        }
        switchTrack(currentTrack)
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
        mediaPlayer.setDataSource(context, getUri(currentTrack))
        mediaPlayer.prepareAsync()
    }

    fun onDestroy() {
        mediaPlayer.release()
    }

    private fun getUri(resId: Int): Uri {
        return Uri.parse("android.resource://${context.packageName}/$resId")
    }
}
