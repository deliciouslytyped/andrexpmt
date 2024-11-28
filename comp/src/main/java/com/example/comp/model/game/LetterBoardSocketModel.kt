package com.example.comp.model.game

import androidx.compose.runtime.mutableStateOf
import com.example.comp.model.owner.Owner

// Game board spaces that can hold a tile
class LetterBoardSocketModel(val gm: GameBoardModel) : Owner, LetterSocketModel() {
    // burned tiles become inaccessible (game rules)
    var burned = mutableStateOf(false)
    override fun accept(tileModel: LetterTileModel) {
        tile = tileModel
        gm.onTilePlaced(this)

    }
}