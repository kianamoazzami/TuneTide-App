package com.example.tunetide.repository

import androidx.room.Query
import com.example.tunetide.database.MusicType
import com.example.tunetide.database.Playback
import com.example.tunetide.database.SpotifyPlaylist
import com.example.tunetide.database.StateType
import com.example.tunetide.database.Timer
import kotlinx.coroutines.flow.Flow

interface IPlaybackRepository {
    suspend fun setPlayback(playback: Playback)

    fun invalidatePlayback()

    fun getPlayback(): Flow<Playback>

    fun getPlayingTimer(): Flow<Timer?>

    fun getPlayingTimerId(): Int

    fun getPlayingMusicSource(): MusicType

    fun getPlayingMusicPlaylistId(): Int

    fun play()

    fun pause()

    fun restartTimer()

    fun getFlowDurationSeconds(): Int

    fun getBreakDurationSeconds(): Int

    fun getStateType(): StateType

    fun getPlayingTimerNumIntervals(): Int

    fun getCurrentInterval(): Int

    fun startNextInterval()
}

class PlaybackRepository(private val playbackDao: PlaybackDao): IPlaybackRepository {
    override suspend fun setPlayback(playback: Playback) = playbackDao.setPlayback(playback)

    override fun invalidatePlayback() = playbackDao.invalidatePlayback()

    override fun getPlayback(): Flow<Playback> = playbackDao.getPlayback()

    override fun getPlayingTimer(): Flow<Timer?> = playbackDao.getPlayingTimer()

    override fun getPlayingTimerId(): Int = playbackDao.getPlayingTimerId()

    override fun getPlayingMusicSource(): MusicType {
        if (playbackDao.getPlayingTimerStateType() == StateType.NONE.value) {
            return MusicType.NO_SOURCE
        } else if (playbackDao.getPlayingTimerStateType() == StateType.FLOW.value &&
            playbackDao.getPlayingTimerMP3FlowMusicPlaylistId() != -1) {
            return MusicType.MP3
        } else if (playbackDao.getPlayingTimerStateType() == StateType.FLOW.value &&
            playbackDao.getPlayingTimerSpotifyFlowMusicPlaylistId() != -1) {
            return MusicType.SPOTIFY
        } else if (playbackDao.getPlayingTimerStateType() == StateType.FLOW.value) {
            return MusicType.NO_SOURCE
        } else if (playbackDao.getPlayingTimerStateType() == StateType.BREAK.value &&
            playbackDao.getPlayingTimerMP3BreakMusicPlaylistId() != -1) {
            return MusicType.MP3
        } else if (playbackDao.getPlayingTimerStateType() == StateType.BREAK.value &&
            playbackDao.getPlayingTimerSpotifyBreakMusicPlaylistId() != -1) {
            return MusicType.SPOTIFY
        } else return MusicType.NO_SOURCE
    }

    override fun getPlayingMusicPlaylistId(): Int {
        if (getPlayingMusicSource() == MusicType.NO_SOURCE) {
            return -1
        } else if (getPlayingMusicSource() == MusicType.MP3 &&
            playbackDao.getPlayingTimerStateType() == StateType.FLOW.value) {
            return playbackDao.getPlayingTimerMP3FlowMusicPlaylistId()
        } else if (getPlayingMusicSource() == MusicType.SPOTIFY &&
            playbackDao.getPlayingTimerStateType() == StateType.FLOW.value) {
            return playbackDao.getPlayingTimerSpotifyFlowMusicPlaylistId()
        } else if (getPlayingMusicSource() == MusicType.MP3 &&
            playbackDao.getPlayingTimerStateType() == StateType.BREAK.value) {
            return playbackDao.getPlayingTimerMP3BreakMusicPlaylistId()
        } else if (getPlayingMusicSource() == MusicType.SPOTIFY &&
            playbackDao.getPlayingTimerStateType() == StateType.BREAK.value) {
            return playbackDao.getPlayingTimerSpotifyBreakMusicPlaylistId()
        } else return -1
    }

    override fun play() {
        if (getPlayingTimerId() != -1) playbackDao.play()
    }

    override fun pause() = playbackDao.pause()

    override fun restartTimer() = playbackDao.restartTimer()

    override fun getFlowDurationSeconds(): Int = playbackDao.getFlowDurationSeconds()

    override fun getBreakDurationSeconds(): Int = playbackDao.getBreakDurationSeconds()

    override fun getStateType(): StateType {
        return StateType.fromValue(playbackDao.getStateType()) ?: StateType.NONE
    }

    override fun getPlayingTimerNumIntervals(): Int = playbackDao.getPlayingTimerNumIntervals()

    override fun getCurrentInterval(): Int = playbackDao.getCurrentInterval()

    override fun startNextInterval() {
        if (getPlayingTimerId() == -1 ||
            getStateType() == StateType.BREAK && playbackDao.getPlayingTimerNumIntervals() == playbackDao.getCurrentInterval()) {
        } else {
            if (getStateType() == StateType.BREAK) playbackDao.startNextFlow()
            else if (getStateType() == StateType.FLOW) playbackDao.startNextBreak()
        }
    }
}