package com.example.comp.data

import com.example.comp.data.retrofit.ScoreAPIFactory
import com.example.comp.model.game.ScoreEntry
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrofitScoreRepository @Inject constructor(val apiFactory: ScoreAPIFactory) {
    val logger = KotlinLogging.logger {}
    val api = apiFactory.api
    fun setURL(baseUrl: String) {
        apiFactory.setURL(baseUrl)
    }
    suspend fun submitScore(name: String, score: Int) {
        api.value?.submitScore(ScoreEntry(name, score)) ?: throw IllegalStateException("API not initialized")
    }

    fun observeScores(): Flow<List<ScoreEntry>> = flow { //TODO why isnt this lazy, i get api not initialized messages without doing anything
        while (true) {
            if (api.value != null) {
            emit(api.value!!.getScores()) //TODO
            } else {
                logger.debug { "API not initialized" }
            }
            delay(3000)
        }
    }


    suspend fun insertTestData() {
        submitScore("juli", 10)
        submitScore("jancsi", 9)
        submitScore("juli", 13)
        submitScore("árpád", 2)
    }
}