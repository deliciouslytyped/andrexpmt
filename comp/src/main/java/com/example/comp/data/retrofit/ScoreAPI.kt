package com.example.comp.data.retrofit

import com.example.comp.model.game.ScoreEntry
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ScoreAPI {
    @POST("/submit_score")
    suspend fun submitScore(@Body score: ScoreEntry)

    @GET("/scores")
    suspend fun getScores(): List<ScoreEntry>
}