package com.fkuper.metronome.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Track::class], version = 2, exportSchema = false)
abstract class MetronomeDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var Instance: MetronomeDatabase? = null

        fun getDatabase(context: Context): MetronomeDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, MetronomeDatabase::class.java, "metronome_database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }

}