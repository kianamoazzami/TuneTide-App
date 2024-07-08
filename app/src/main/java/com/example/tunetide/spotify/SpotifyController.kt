package com.example.tunetide.spotify

import android.content.Context
import android.util.Log
import com.example.tunetide.MainActivity
import androidx.lifecycle.lifecycleScope
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.sdk.android.auth.AccountsQueryParameters
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SpotifyController(val mainContext: MainActivity) {
    // All new spotifyController functions for public use will need to go through a connectAndExecute call right now

    private val clientId = "cb2af3cb9add453d8a18f97e2aae117f"
    private val redirectUri = "tunetide://test"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private suspend fun connectToAppRemote(showAuthView: Boolean): SpotifyAppRemote? =
        suspendCoroutine { cont: Continuation<SpotifyAppRemote> ->
            SpotifyAppRemote.connect(
                mainContext,
                ConnectionParams.Builder(clientId)
                    .setRedirectUri(redirectUri)
                    .showAuthView(showAuthView)
                    .build(),
                object : Connector.ConnectionListener {
                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        cont.resume(spotifyAppRemote)
                    }

                    override fun onFailure(error: Throwable) {
                        cont.resumeWithException(error)
                    }
                })
        }
    fun connectPersist(): String {

        SpotifyAppRemote.disconnect(spotifyAppRemote)
        runBlocking {
            spotifyAppRemote = connectToAppRemote(true)
        }
        if (spotifyAppRemote == null) {return "FUCKED IT AGAIN"}
        else { return "OMG UR AMAZEBALLS"}
    }

    private fun connectAndExecute(operation: () -> Any?): Any? {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(mainContext, connectionParams, object : Connector.ConnectionListener {
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

        SpotifyAppRemote.connect(mainContext, connectionParams, object : Connector.ConnectionListener {
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