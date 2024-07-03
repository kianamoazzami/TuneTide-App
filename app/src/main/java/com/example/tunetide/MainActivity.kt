package com.example.tunetide

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tunetide.ui.TuneTideApp
import com.example.tunetide.ui.theme.TuneTideTheme
/*
import com.example.tunetide.spotify.SpotifyController
*/

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TuneTideTheme {
                Log.d("TuneTideMainActivity", "calling app")
                TuneTideApp()
            }
        }

    }

    // TODO @ERICA likely need to inject this via a dependency in it's own repo (see AppContainer.kt)
    override fun onStart() {
        // probably don't want to prompt for spotify connection right as app starts
        // could put as separate option
        super.onStart()
        //mainSpotifyController.Connect(this);
    }

    override fun onStop() {
        super.onStop()
        //mainSpotifyController.Disconnect();
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TuneTideTheme {
    }
}