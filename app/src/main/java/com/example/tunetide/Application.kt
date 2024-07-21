package com.example.tunetide

import android.app.Application
import android.util.Log
import com.example.tunetide.spotify.SpotifyController

class Application : Application() {

    // AppContainer instance (the other classes use this for dependency injection)
    lateinit var container: AppContainer

    private val mainSpotifyController : SpotifyController = SpotifyController(this);

    override fun onCreate() {
        super.onCreate()
        Log.d("TuneTideApplication", "Initializing container")
        container = AppDataContainer(this)
        mainSpotifyController.loadPlaylists(container.spotifyRepository)



    }
}