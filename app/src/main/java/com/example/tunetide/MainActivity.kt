package com.example.tunetide

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tunetide.ui.theme.TuneTideTheme
import com.example.tunetide.ui.HomePage
import com.example.tunetide.spotify.SpotifyController

class MainActivity : ComponentActivity() {
    private var homePage: HomePage = HomePage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TuneTideTheme {
                homePage.layout();
            }
        }

    }

    override fun onStart() {
        // probably don't want to prompt for spotify connection right as app starts
        // could put as separate option
        super.onStart()
        //SpotifyController.Connect(this);
    }

    override fun onStop() {
        super.onStop()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TuneTideTheme {
        HomePage()
    }
}