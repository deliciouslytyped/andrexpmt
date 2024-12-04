package com.example.comp.model.game.concept

import com.example.comp.model.game.concept.owner.Owner

//A game is a collection of token owners which can accept or reject accepting a token
//TODO is there anything to actually put in here?
interface GameModel {
//class GameModel(private val parts: List<Owner<Token>>) {
    fun newGame()
    fun checkGameOver(): Boolean
}