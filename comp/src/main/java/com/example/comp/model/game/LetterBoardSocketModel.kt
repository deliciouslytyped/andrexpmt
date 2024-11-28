package com.example.comp.model.game

import androidx.compose.runtime.mutableStateOf

// Game board spaces that can hold a tile
class LetterBoardSocketModel(val gm: GameBoardModel) : LetterSocketModel() {
    // burned tiles become inaccessible (game rules)
    var burned = mutableStateOf(false)
    override fun accept(tileModel: LetterTileModel) {
        tile = tileModel
        gm.onTilePlaced(this)

    }
}