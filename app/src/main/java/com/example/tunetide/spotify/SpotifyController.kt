package com.example.tunetide.spotify

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.tunetide.database.SpotifyPlaylist
import com.example.tunetide.repository.SpotifyRepository
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.ContentApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.SpotifyDisconnectedException
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.Capabilities
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ListItem
import com.spotify.protocol.types.ListItems
import com.spotify.protocol.types.PlayerContext
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.TimeUnit
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

    private var isSpotifyInstalled = true;
    private var playerStateSubscription: Subscription<PlayerState>? = null

    class NowPlaying {
        var playlistUrl: MutableStateFlow<String> = MutableStateFlow<String>("No Playlist Selected")
        var title : MutableStateFlow<String> = MutableStateFlow<String>("No Song Playing")
        var artist : MutableStateFlow<String> = MutableStateFlow<String>("No Song Playing")
        var imgURL : String = ""
        var paused : Boolean = true;
        var isShuffling : Boolean = false;
        /* to add: track bar?*/
    }

    var currentTrack : NowPlaying = NowPlaying()
    private val errorCallback = { throwable: Throwable -> logError(throwable) }

    private val playerStateEventCallback = Subscription.EventCallback<PlayerState> { playerState ->
        currentTrack.title.value = playerState.track.name
        currentTrack.artist.value = playerState.track.artist.name
        currentTrack.paused = playerState.isPaused
        currentTrack.isShuffling = playerState.playbackOptions.isShuffling
    }

    init {
        connectPersist()
    }

    private suspend fun connectToAppRemote(showAuthView: Boolean): SpotifyAppRemote? =
        suspendCoroutine { cont: Continuation<SpotifyAppRemote> ->
            SpotifyAppRemote.setDebugMode(true)
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
                        logError(error)
                        isSpotifyInstalled = false
                        cont.resumeWithException(error)
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

    private fun onConnected() {
        Log.d("Spotify Controller", "Connection alive")
        onSubscribedToPlayerState()
        //onSubscribedToPlayerContext()
    }

    // Suspending function to wait for connection before API call
    // Can be used publicly to suspend and wait for spotify and/or check connection
    // may want private in future
    suspend fun waitForConnection() {
       if (isSpotifyInstalled) {
           var waited = 0;
           while (spotifyAppRemote == null && waited < 5) {
               delay(2000)
               Log.d("Spotify Controller", "Waiting for Connection to use spotify...")
               waited++
           }

           if (waited >= 5) {
               Log.e("Spotify Controller", "Connection to Spotify timed out, trying again...")
               connectPersist()

               var waited = 0;
               while (spotifyAppRemote == null && waited < 5) {
                   delay(2000)
                   Log.d("Spotify Controller", "Waiting for Connection to use spotify...")
                   waited++
               }

               if (waited >= 5) {
                   Log.e("Spotify Controller", "Still could not connect")
               }
           }
       } else {
           Log.e("Spotify Controller", "Spotify is not installed on this device")
       }
    }

    fun setPlaylistUrl(url : String){
        currentTrack.playlistUrl.value = url
    }

    fun play() {
        playUri(currentTrack.playlistUrl.value)
    }

    fun playUri(uri: String) {
        fun inlinePlayUri() {
            spotifyAppRemote?.let {
                it.playerApi.play(uri)
                Log.d("SpotifyController", "Playing content by URL")

                it.playerApi.subscribeToPlayerState().setEventCallback {
                    val track: Track = it.track

                    currentTrack.title.value = track.name
                    currentTrack.artist.value = track.artist.name
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

    fun resumePlayback() {
        CoroutineScope(Dispatchers.Main).launch {
            if (spotifyAppRemote == null) {
                waitForConnection()
            }
            spotifyAppRemote?.let {
                it.playerApi
                    .playerState
                    .setResultCallback { playerState ->
                        if (playerState.isPaused && playerState.track.name == currentTrack.title.value) {
                            it.playerApi
                                .resume()
                                .setResultCallback { Log.d("Spotify Controller", "Resumed playback") }
                                .setErrorCallback(errorCallback)
                        } else {
                            play()
                            //Log.e("Spotify Controller", "Error resuming playback, no music qeued or music still playing")
                        }
                    }
            }
        }
    }

    fun skipToNextSong() {
        CoroutineScope(Dispatchers.Main).launch {
            if (spotifyAppRemote == null) {
                waitForConnection()
            }
            spotifyAppRemote?.let {
                it.playerApi.skipNext()
                Log.d("Spotify Controller", "Skipped track")
            }
        }
    }

    fun toggleShuffle() {
        CoroutineScope(Dispatchers.Main).launch {
            waitForConnection()
            spotifyAppRemote?.playerApi
            ?.toggleShuffle()
            ?.setResultCallback {
                Log.d("Spotify Controller", "Set Shuffle")
            }
            ?.setErrorCallback(errorCallback)
        }
    }

    suspend fun getCurrentTrackName() : String {
        var waited = 0
        while (playerStateSubscription == null && waited < 6) {
            Log.d("SpotifyController", "Waiting for subscription to PlayerState...")
            delay(2000)
            waited++
        }

        if (waited >= 6) {
            Log.e("SpotifyController", "Connection to PlayerState Timed out.")
            return ""
        }
        return currentTrack.title.value
    }

    suspend fun getCurrentTrackArtist() : String {
        var waited = 0
        while (playerStateSubscription == null && waited < 6) {
            Log.d("SpotifyController", "Waiting for subscription to PlayerState...")
            delay(2000)
            waited++
        }

        if (waited >= 6) {
            Log.e("SpotifyController", "Connection to PlayerState Timed out.")
            return ""
        }
        return currentTrack.title.value
    }

    public fun disconnect() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    private fun logError(throwable: Throwable) {
        Toast.makeText(mainContext, "Could not connect to Spotify", Toast.LENGTH_SHORT).show()
        Log.e("SpotifyController", "", throwable)
    }

    //TODO: below functions.. track updates in-time, track browsing

    private fun convertToList(inputItems: ListItems?): List<ListItem> {
        return if (inputItems?.items != null) {
            inputItems.items.toList()
        } else {
            emptyList()
        }
    }

    private suspend fun loadChildren(appRemote: SpotifyAppRemote?, parent: ListItem): ListItems? =
        suspendCoroutine { cont ->
            appRemote?.contentApi?.getChildrenOfItem(parent, 6, 0)?.setResultCallback { listItems -> cont.resume(listItems) }
                ?.setErrorCallback { throwable ->
                    errorCallback.invoke(throwable)
                    cont.resumeWithException(throwable)
                }
        }

    private suspend fun loadRootRecommendations(appRemote: SpotifyAppRemote?): ListItems? =
        suspendCoroutine { cont ->
                appRemote?.contentApi?.getRecommendedContentItems(ContentApi.ContentType.FITNESS)?.setResultCallback { listItems -> cont.resume(listItems) }
                    ?.setErrorCallback { throwable ->
                        errorCallback.invoke(throwable)
                        cont.resumeWithException(throwable)
                    }

        }

    // Call at start
    fun loadPlaylists(spotifyRepository : SpotifyRepository) {
        CoroutineScope(Dispatchers.Main).launch {
            waitForConnection()
            spotifyAppRemote.let {
                CoroutineScope(Dispatchers.Main).launch {
                    val combined = ArrayList<ListItem>(50)
                    val listItems = loadRootRecommendations(it)
                    listItems?.apply {
                        var added = 0;
                        for (i in items.indices) {
                            if (items[i].playable) {
                                combined.add(items[i])
                            } else {
                                val children: ListItems? = loadChildren(it, items[i])
                                combined.addAll(convertToList(children))
                                children?.apply{
                                    for (checked in children.items.indices) {
                                        if (children.items[checked].playable) {
                                            val playlist: SpotifyPlaylist = SpotifyPlaylist(
                                                added,
                                                children.items[checked].title,
                                                children.items[checked].uri,
                                                children.items[checked].imageUri.toString()
                                            )
                                            spotifyRepository.insertPlaylist(playlist)
                                            added++
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onSubscribedToPlayerState() {
        playerStateSubscription = cancelAndResetSubscription(playerStateSubscription)

        fun inlineSubscribe() {
            playerStateSubscription = spotifyAppRemote
                ?.playerApi
                ?.subscribeToPlayerState()
                ?.setEventCallback(playerStateEventCallback)
                ?.setLifecycleCallback(
                    object : Subscription.LifecycleCallback {
                        override fun onStart() {
                            //logMessage("Event: start")
                            Log.d("SpotifyController", "Subscribed to playerState")
                        }

                        override fun onStop() {
                            Log.d("SpotifyController", "Unsubscribed from playerState")
                        }
                    })
                ?.setErrorCallback {
                    Log.e("SpotifyController", "Error subscribing to playerstate")
                } as Subscription<PlayerState>
        }

        if (spotifyAppRemote != null) {
            inlineSubscribe()
        } else {
            CoroutineScope(Dispatchers.Main).launch{
                waitForConnection()
                inlineSubscribe()
            }
        }

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
