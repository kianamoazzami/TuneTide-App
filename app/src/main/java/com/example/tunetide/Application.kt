package com.example.tunetide

import android.app.Application
import android.util.Log
import com.example.tunetide.spotify.SpotifyController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Application : Application() {

    // AppContainer instance (the other classes use this for dependency injection)
    lateinit var container: AppContainer

    private val mainSpotifyController : SpotifyController = SpotifyController(this);

    override fun onCreate() {
        super.onCreate()
        Log.d("TuneTideApplication", "Initializing container")
        container = AppDataContainer(this)
        mainSpotifyController.loadPlaylists(container.spotifyRepository)

        mainSpotifyController.playUri("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")

        CoroutineScope(Dispatchers.Main).launch {
            Log.d("ERICA TEST", mainSpotifyController.getCurrentTrackName())
        }



    }
}