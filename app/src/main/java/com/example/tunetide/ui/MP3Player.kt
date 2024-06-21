package com.example.tunetide.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tunetide.R
import androidx.compose.ui.res.painterResource
import com.example.tunetide.ui.manager.MP3PlayerManager

class MP3Player (var context: Context){
    private var mp3PlayerManager: MP3PlayerManager = MP3PlayerManager(context)

    @Composable
    fun layout() {
        val isPlaying by mp3PlayerManager::isPlaying

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            IconButton(onClick = { mp3PlayerManager.switchTrack(R.raw.flowmusic)  }) {
                Icon(
                    painter = painterResource(id = R.drawable.flowbutton),
                    contentDescription = "Flow", tint = Color.Unspecified
                )
            }
            IconButton(onClick = { mp3PlayerManager.togglePlayPause() }) {
                val playPauseIcon = if (isPlaying) R.drawable.musicpausebutton else R.drawable.musicplaybutton
                Icon(
                    painter = painterResource(id = playPauseIcon),
                    contentDescription = "Play/Pause", tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = { mp3PlayerManager.switchTrack(R.raw.breakmusic) }) {
                Icon(
                    painter = painterResource(id = R.drawable.breakbutton),
                    contentDescription = "Break", tint = Color.Unspecified
                )
            }
        }
    }

    fun onDestroy() {
        mp3PlayerManager.onDestroy()
    }
}
