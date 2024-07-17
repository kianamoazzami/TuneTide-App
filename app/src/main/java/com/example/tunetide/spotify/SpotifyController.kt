package com.example.tunetide.spotify

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tunetide.ui.TuneTideApp
import com.example.tunetide.ui.theme.TuneTideTheme
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.Capabilities
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//Controller needs to be initialized with reference context..
// 'application' should give application context
// ie val controller : SpotifyController = SpotifyController(application)
class SpotifyController(private val mainContext: Context) {
    // All new spotifyController functions for public use will need to go through a connectAndExecute call right now
    private val clientId = "cb2af3cb9add453d8a18f97e2aae117f"
    private val redirectUri = "tunetide://test"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private var playerStateSubscription: Subscription<PlayerState>? = null
    private var playerContextSubscription: Subscription<PlayerContext>? = null
    private var capabilitiesSubscription: Subscription<Capabilities>? = null

    val trackLock : Mutex = Mutex(false)

    class NowPlaying {
        var title : String = ""
        var artist : String = ""
        var imgURL : String = ""
        var paused : Boolean = true;
        /* to add: track bar?*/
    }

    private var currentTrack : NowPlaying = NowPlaying()
    private val errorCallback = { throwable: Throwable -> logError(throwable) }

    init {
        connectPersist()
    }

    private suspend fun connectToAppRemote(showAuthView: Boolean): SpotifyAppRemote? =
        suspendCoroutine { cont: Continuation<SpotifyAppRemote> ->
            SpotifyAppRemote.connect(
                mainContext,
                ConnectionParams.Builder(clientId)
                    .setRedirectUri(redirectUri)
                    .showAuthView(showAuthView)
                    .build(),
                object : Connector.ConnectionListener {
                    override fun onConnected(appRemote: SpotifyAppRemote) {
                        cont.resume(appRemote)
                    }

                    override fun onFailure(error: Throwable) {
                        cont.resumeWithException(error)
                        logError(error)
                    }
                })
        }

    private fun connectPersist() {
        SpotifyAppRemote.disconnect(spotifyAppRemote)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                spotifyAppRemote = connectToAppRemote(true)
                onConnected()
            } catch (error: Throwable) {
                logError(error)
            }
            if (spotifyAppRemote != null) {
                Log.d("TestErica", "Connection alive")
            }
        }
    }

    fun playUri(uri: String) {
        fun inlinePlayUri() {
            spotifyAppRemote?.let {
                it.playerApi.play(uri)
                Log.d("SpotifyController", "Playing content by URL")

                it.playerApi.subscribeToPlayerState().setEventCallback {
                    val track: Track = it.track

                    currentTrack.title = track.name
                    currentTrack.artist = track.artist.name
                    currentTrack.imgURL = track.imageUri.toString()
                    currentTrack.paused = false

                    Log.d(
                        "SpotifyController",
                        "Playing : " + track.name + " by " + track.artist.name
                    )
                }
            }
        }

        // If we can run sequentially, do it, if not send to wait
        // Sequential has better chance of updating currentTrack faster
        if (spotifyAppRemote != null) {
            inlinePlayUri()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                waitForConnection()
                inlinePlayUri()
            }
        }
    }

    // Pause any current spotify playback
    fun pause() {
        CoroutineScope(Dispatchers.Main).launch {
            if (spotifyAppRemote == null) {
                waitForConnection()
            }
            spotifyAppRemote?.let {
                it.playerApi.pause()
                currentTrack.paused = true
                Log.d("Spotify Controller", "Music Paused")
            }
        }
    }

    //TODO: have recall of title reflect without delay from coroutine
    fun getCurrentTrackName() : String {
        return currentTrack.title
    }

    fun getCurrentTrackArtist() : String {
        return currentTrack.artist
    }

    private fun onConnected() {
        Log.d("Spotify Controller", "Connection alive")
        //onSubscribedToPlayerState()
        //onSubscribedToPlayerContext()
    }


    // Suspending function to wait for connection before API call
    // Can be used publicly to suspend and wait for spotify and/or check connection
    // may want private in future
    suspend fun waitForConnection() {
        var waited = 0;
        while (spotifyAppRemote == null && waited < 5) {
            delay(2000)
            Log.d("Spotify Controller", "Waiting for Connection to play URI..")
            waited++
        }

        if (waited >= 5) {
            Log.e("Spotify Controller", "Connection to Spotify timed out")
            throw(SpotifyDisconnectedException())
        }
    }

    public fun disconnect() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    private fun logError(throwable: Throwable) {
        Toast.makeText(mainContext, "An Error Has occured.", Toast.LENGTH_SHORT).show()
        Log.e("Spotify Controller", "", throwable)
    }

    //TODO: below functions.. track updates in-time, track browsing

    private val playerContextEventCallback = Subscription.EventCallback<PlayerContext> { playerContext ->
       //TODO: fill in
    }

    private fun onSubscribedToPlayerContext() {

    }

    private val playerStateEventCallback = Subscription.EventCallback<PlayerState> { playerState ->
        //TODO: @erica fill in
    }

    private fun <T : Any?> cancelAndResetSubscription(subscription: Subscription<T>?): Subscription<T>? {
        return subscription?.let {
            if (!it.isCanceled) {
                it.cancel()
            }
            null
        }
    }


}
