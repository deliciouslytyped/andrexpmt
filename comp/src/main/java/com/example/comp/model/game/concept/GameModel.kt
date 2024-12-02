package com.example.comp.model.game.concept

import com.example.comp.model.game.concept.owner.Owner

//A game is a collection of token owners which can accept or reject accepting a token
class GameModel(private val parts: List<Owner<Token>>) {
}