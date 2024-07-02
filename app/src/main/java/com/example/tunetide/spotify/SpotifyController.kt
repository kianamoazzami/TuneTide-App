package com.example.tunetide.spotify

import android.content.Context
import android.util.Log
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

private const val clientId = "cb2af3cb9add453d8a18f97e2aae117f"
private const val redirectUri = "tunetide://test"
private var spotifyAppRemote: SpotifyAppRemote? = null


class SpotifyController(val mainContext: Context) {

    private fun connectAndExecute(operation: () -> Any?): Any? {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        var returnVal: Any? = null

        SpotifyAppRemote.connect(mainContext, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                // TODO: see if you can make appRemote persist.. otherwise have to do this wrapper way yuck
                Log.d("MainActivity", "Connected! Yay!")
                returnVal = operation()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })

        return returnVal;

    }

    // overload connect to take function with parameters
    // TODO: this is clunky. I can't find a way to have kotlin take a function that may or may not have parameters
    private fun connectAndExecute(operation: (Any?) -> Unit, params: Any? = null): Any? {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        var returnVal: Any? = null

        SpotifyAppRemote.connect(mainContext, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                if (params != null) {
                    returnVal = operation(params)
                }
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })

        return returnVal;
    }

    fun disconnect() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
    fun playSamplePlaylist() {
        fun playSampleWrapper() {
            Log.d("MainActivity", "pressed play")
            spotifyAppRemote?.let {
                // Play a playlist
                val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
                it.playerApi.play(playlistURI)
                Log.d("MainActivity", "PlayerAPI called")
                // Subscribe to PlayerState
                it.playerApi.subscribeToPlayerState().setEventCallback {
                    val track: Track = it.track
                    Log.d("MainActivity", track.name + " by " + track.artist.name)
                }
            }
        }
        connectAndExecute(::playSampleWrapper)
    }
}