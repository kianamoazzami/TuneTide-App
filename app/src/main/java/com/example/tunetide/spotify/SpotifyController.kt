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
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SpotifyController(connectedRemote : SpotifyAppRemote) {

    private var spotifyAppRemote: SpotifyAppRemote? = null

    init {
        spotifyAppRemote = connectedRemote
    }
    fun isConnected() : Boolean {
        return spotifyAppRemote == null;
    }

}


/*
class SpotifyController() : ComponentActivity() {
    // All new spotifyController functions for public use will need to go through a connectAndExecute call right now

    private val clientId = "cb2af3cb9add453d8a18f97e2aae117f"
    private val redirectUri = "tunetide://test"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private var playerStateSubscription: Subscription<PlayerState>? = null
    private var playerContextSubscription: Subscription<PlayerContext>? = null
    private var capabilitiesSubscription: Subscription<Capabilities>? = null

    private val errorCallback = { throwable: Throwable -> logError(throwable) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SpotifyController", "onCreate Called")
        super.onCreate(savedInstanceState)
        setContent {
            TuneTideTheme {
                Log.d("SpotifyController", "calling Theme")
                TuneTideApp()
            }
        }

        SpotifyAppRemote.setDebugMode(true)
        connectPersist();
    }

    override fun onStart() {
        super.onStart()
        //playUri("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
    }

    private fun playUri(uri: String) {
        assertAppRemoteConnected()
            .playerApi
            .play(uri)
            .setResultCallback { logMessage("play") }
            .setErrorCallback(errorCallback)
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    private suspend fun connectToAppRemote(showAuthView: Boolean): SpotifyAppRemote? =
        suspendCoroutine { cont: Continuation<SpotifyAppRemote> ->
            SpotifyAppRemote.connect(
                applicationContext,
                ConnectionParams.Builder(clientId)
                    .setRedirectUri(redirectUri)
                    .showAuthView(showAuthView)
                    .build(),
                object : Connector.ConnectionListener {
                    override fun onConnected(appRemote: SpotifyAppRemote) {
                        cont.resume(appRemote)
                        spotifyAppRemote = appRemote
                    }

                    override fun onFailure(error: Throwable) {
                        cont.resumeWithException(error)
                        logError(error)
                    }
                })
        }

    private fun logError(throwable: Throwable) {
        Toast.makeText(this, "An Error Has Occured.", Toast.LENGTH_SHORT).show()
        Log.e("SpotifyController", "", throwable)
    }
    fun connectPersist(): String {
        SpotifyAppRemote.disconnect(spotifyAppRemote)
        lifecycleScope.launch {
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
        if (spotifyAppRemote != null) {
            Log.d("TestErica", "AYAY Connection persisted")
        }
        return "Im so tired"
    }

    private fun onConnected() {
        //onSubscribedToPlayerState()
        //onSubscribedToPlayerContext()
    }


    private val playerContextEventCallback = Subscription.EventCallback<PlayerContext> { playerContext ->
       //TODO: fill in
    }

    private fun onSubscribedToPlayerContext() {
        playerContextSubscription = cancelAndResetSubscription(playerContextSubscription)

        playerContextSubscription = assertAppRemoteConnected()
            .playerApi
            .subscribeToPlayerContext()
            .setEventCallback(playerContextEventCallback)
            .setErrorCallback { throwable ->
                logError(throwable)
            } as Subscription<PlayerContext>
    }
    private fun onSubscribedToPlayerState() {
        playerStateSubscription = cancelAndResetSubscription(playerStateSubscription)

        playerStateSubscription = assertAppRemoteConnected()
            .playerApi
            .subscribeToPlayerState()
            .setEventCallback(playerStateEventCallback)
            .setLifecycleCallback(
                object : Subscription.LifecycleCallback {
                    override fun onStart() {
                        logMessage("Event: start")
                    }

                    override fun onStop() {
                        logMessage("Event: end")
                    }
                })
            .setErrorCallback {
                logError(it)
            } as Subscription<PlayerState>
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

    private fun assertAppRemoteConnected(): SpotifyAppRemote {
        spotifyAppRemote?.let {
            if (it.isConnected) {
                return it
            }
        }
        Log.e("SpotifyController", "Spotify Disconnected")
        throw SpotifyDisconnectedException()
    }

    private fun logMessage(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, duration).show()
        Log.d("SpotifyController", msg)
    }

    private fun connectAndExecute(operation: () -> Any?): Any? {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(application, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                // TODO: see if you can make appRemote persist.. otherwise have to do this wrapper way yuck
                Log.d("MainActivity", "Connected! Yay!")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })

        return null;
    }

    // overload connect to take function with parameters
    // TODO: this is clunky. I can't find a way to have kotlin take a function that may or may not have parameters
    private fun connectAndExecute(operation: (Any?) -> Unit, params: Any? = null): Any? {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(application, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })

        if (params != null) {
            return operation(params)
        }

        return null;
    }

    fun disconnect() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    fun playPlaylistURI(uri: String) {
        fun playURIWrapper() {
            spotifyAppRemote?.let {
                // Play a playlist

                it.playerApi.play(uri)
                Log.d("SpotifyController", "Playing Playlist by URL")

                it.playerApi.subscribeToPlayerState().setEventCallback {
                    val track: Track = it.track
                    Log.d("SpotifyController", "Playing : " + track.name + " by " + track.artist.name)
                }
            }
        }
        connectAndExecute(::playURIWrapper)
    }

    fun testReturn() {

        fun foo (): String {
            return "YAY GOT RESPONSE"
        }
        Log.d("TestReturn", connectAndExecute(::foo).toString())
    }

    fun pause() {
        fun pauseWrapper() {
            spotifyAppRemote?.let {
                // Play a playlist

                it.playerApi.pause();
                Log.d("SpotifyController", "Pausing music")

            }
        }
        connectAndExecute(::pauseWrapper)
    }

    /*
    fun getCurrentTrackName() : String {

        fun getTrackNameWrapper(): String {
            var returnVal: String = ""
            spotifyAppRemote?.let {
                // Play a playlist
                it.playerApi.subscribeToPlayerState().setEventCallback {
                    val track: Track = it.track
                    returnVal = track.name
                }
            }
            return returnVal
        }
        return connectAndExecute(::getTrackNameWrapper)
    }
    */
    fun getCurrentTrackArtist() {

    }

}

*/