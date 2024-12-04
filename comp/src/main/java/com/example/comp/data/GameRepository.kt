package com.example.comp.data

//TODO we use event sourcing-type solution right now because the game model is imperative?
interface GameRepository {
    //fun observeGameMoves(): Flow<List<Moves>>

    //suspend fun saveGame(gm: GameModel)
    suspend fun saveMove()
}