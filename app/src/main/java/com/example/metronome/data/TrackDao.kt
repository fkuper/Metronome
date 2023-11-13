package com.example.metronome.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: Track)

    @Update
    suspend fun update(track: Track)

    @Delete
    suspend fun delete(track: Track)

    @Query("SELECT * FROM tracks ORDER BY title ASC")
    fun getAllTracks(): Flow<List<Track>>

}