package com.example.tunetide.database
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "MP3File",
    foreignKeys = [ForeignKey(
        entity = MP3Playlist::class,
        parentColumns = ["playlist_id"],
        childColumns = ["playlist_id"],
        onDelete = ForeignKey.NO_ACTION
    )])
data class MP3File (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "file_id")
    @NonNull
    val fileId: Int = 0,

    @ColumnInfo(name = "playlist_id")
    @NonNull
    val playlistId: Int,

    @ColumnInfo(name = "file_name")
    val fileName: String,

    @ColumnInfo(name = "file_path")
    @NonNull
    val filePath: String,

    @ColumnInfo(name = "file_cover")
    val fileCover: String
)

/*
TableInfo{name='MP3File',
columns={file_cover=Column{name='file_cover', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='undefined'},
file_path=Column{name='file_path', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='undefined'},
playlist_id=Column{name='playlist_id', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='undefined'},
file_name=Column{name='file_name', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='undefined'},
file_id=Column{name='file_id', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='undefined'}},
foreignKeys=[ForeignKey{referenceTable='MP3Playlist', onDelete='NO ACTION +', onUpdate='NO ACTION', columnNames=[playlist_id], referenceColumnNames=[playlist_id]}], indices=[]}

Found:
TableInfo{name='MP3File', columns={
file_id=Column{name='file_id', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='undefined'},
playlist_id=Column{name='playlist_id', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='undefined'},
file_name=Column{name='file_name', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='undefined'},
file_path=Column{name='file_path', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='undefined'},
file_cover=Column{name='file_cover', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0, defaultValue='undefined'}},
foreignKeys=[ForeignKey{referenceTable='MP3Playlist', onDelete='NO ACTION +', onUpdate='NO ACTION', columnNames=[playlist_id], referenceColumnNames=[playlist_id]}], indices=[]}
                                                                                                    	at androidx.room.RoomOpenHelper.onCreate(RoomOpenHelper.kt:74)                                                                                                 	at androidx.room.RoomOpenHelper.onCreate(RoomOpenHelper.kt:74)
*/