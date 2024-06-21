package com.example.tunetide.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tunetide.repository.MP3FileDao
import com.example.tunetide.repository.TimerDao

@Database(entities = [MP3File::class,
                      Timer::class], version = 1)
abstract class TuneTideDatabase : RoomDatabase() {
    abstract fun mp3FileDao(): MP3FileDao
    abstract fun timerDao(): TimerDao

    companion object {
        @Volatile
        private var Instance: TuneTideDatabase? = null

        fun getDatabase(context: Context): TuneTideDatabase {
            // if instance (of database) is not null, return, else create a new instance
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TuneTideDatabase::class.java, "tunetide_database")
                    .createFromAsset("starterDatabase.db") // TODO
                    .build()
                    .also { Instance = it }
            }
        }
    }
}