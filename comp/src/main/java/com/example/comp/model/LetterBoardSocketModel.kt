package com.example.comp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.comp.model.index.cartesian.findLongestWords

// Game board spaces that can hold a tile
class LetterBoardSocketModel(val gm: GameBoardModel) : TileOwner, LetterSocketModel() {
    // burned tiles become inaccessible (game rules)
    var burned = mutableStateOf(false)
    override fun accept(tileModel: LetterTileModel) {
        tile = tileModel
        gm.onTilePlaced(this)

    }
}