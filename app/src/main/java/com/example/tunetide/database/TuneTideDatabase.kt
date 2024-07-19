package com.example.tunetide.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tunetide.repository.MP3Dao
import com.example.tunetide.repository.PlaybackDao
import com.example.tunetide.repository.SpotifyDao
import com.example.tunetide.repository.TimerDao

@Database(entities = [MP3File::class,
                      Timer::class,
                      MP3Playlist::class,
                      SpotifyPlaylist::class,
                      Playback::class], version = 1)
abstract class
TuneTideDatabase : RoomDatabase() {
    abstract fun mp3Dao(): MP3Dao
    abstract fun timerDao(): TimerDao
    abstract fun spotifyDao(): SpotifyDao
    abstract fun playbackDao(): PlaybackDao

    companion object {
        @Volatile
        private var Instance: TuneTideDatabase? = null

        fun getDatabase(context: Context): TuneTideDatabase {
            // if instance (of database) is not null, return, else create a new instance
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TuneTideDatabase::class.java, "tunetide_database")
                    .createFromAsset("starterDatabase.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}