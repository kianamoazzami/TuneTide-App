package com.example.tunetide.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.tunetide.database.Playback
import com.example.tunetide.database.Timer
import com.example.tunetide.database.SpotifyPlaylist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaybackDao {
    // TODO @MIA to maintain singleton in DB, must assert that id = 1 so it will update, not insert
    @Update
    suspend fun setPlayback(playback: Playback)

    @Query("UPDATE Playback" +
            " SET timer_id = -1, state_type = 2, current_interval = -1, current_interval_remaining_seconds = 0, is_playing = 0" +
            " WHERE id = 1")
    fun invalidatePlayback()

    @Query("SELECT * FROM Playback")
    fun getPlayback(): Flow<Playback>

    @Query("SELECT * FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getPlayingTimer(): Flow<Timer?>

    @Query("SELECT timer_id FROM Playback WHERE id == 1")
    fun getPlayingTimerId(): Int

    @Query("UPDATE Playback SET is_playing = 1 WHERE id = 1")
    fun play()

    @Query("UPDATE Playback SET is_playing = 0 WHERE id = 1")
    fun pause()

    @Query("UPDATE Playback" +
            " SET state_type = 0, current_interval = 1, current_interval_remaining_seconds = " +
            "(SELECT flow_music_duration_seconds FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)), is_playing = 1" +
            " WHERE id = 1")
    fun restartTimer()

    @Query("SELECT flow_music_duration_seconds FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getFlowDurationSeconds(): Int

    @Query("SELECT break_music_duration_seconds FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getBreakDurationSeconds(): Int

    @Query("SELECT state_type FROM Playback WHERE id == 1")
    fun getStateType(): Int

    @Query("UPDATE Playback" +
            " SET state_type = 0, current_interval = (SELECT current_interval FROM Playback WHERE id == 1) + 1, " +
            "current_interval_remaining_seconds = (SELECT flow_music_duration_seconds FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)), is_playing = 1" +
            " WHERE id = 1")
    fun startNextFlow()

    @Query("UPDATE Playback" +
            " SET state_type = 1, " +
            "current_interval_remaining_seconds = (SELECT break_music_duration_seconds FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)), is_playing = 1" +
            " WHERE id = 1")
    fun startNextBreak()

    // region helpers
    @Query("SELECT spotify_flow_music_playlist_id FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getPlayingTimerSpotifyFlowMusicPlaylistId(): Int

    @Query("SELECT mp3_flow_music_playlist_id FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getPlayingTimerMP3FlowMusicPlaylistId(): Int

    @Query("SELECT spotify_break_music_playlist_id FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getPlayingTimerSpotifyBreakMusicPlaylistId(): Int

    @Query("SELECT mp3_break_music_playlist_id FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getPlayingTimerMP3BreakMusicPlaylistId(): Int

    @Query("SELECT state_type FROM Playback WHERE id == 1")
    fun getPlayingTimerStateType(): Int

    @Query("SELECT current_interval FROM Playback WHERE id == 1")
    fun getCurrentInterval(): Int

    @Query("SELECT num_intervals FROM Timer WHERE timer_id == (SELECT timer_id FROM Playback WHERE id == 1)")
    fun getPlayingTimerNumIntervals(): Int
    // endregion
}