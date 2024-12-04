package com.example.comp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.comp.model.game.ScoreEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT name, score FROM Score ORDER BY score DESC ")
    fun getScores(): Flow<List<ScoreEntry>>

    @Insert
    suspend fun insertScore(s: Score)
}