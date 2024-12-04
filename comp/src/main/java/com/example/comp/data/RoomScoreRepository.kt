package com.example.comp.data

import com.example.comp.data.room.Score
import com.example.comp.data.room.ScoreDao
import com.example.comp.model.game.ScoreEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class RoomScoreRepository @Inject constructor(val scoreDao: ScoreDao) {

    suspend fun submitScore(name: String, score: Int) {
        scoreDao.insertScore(ScoreEntry(name, score).asScore())
    }

    fun observeScores(): Flow<List<ScoreEntry>> =
        scoreDao.getScores().distinctUntilChanged()

    suspend fun insertTestData() {
        submitScore("juli", 10)
        submitScore("jancsi", 9)
        submitScore("juli", 13)
        submitScore("árpád", 2)
    }
}

private fun ScoreEntry.asScore(): Score {
    return Score(null, this.name, this.score)
}
