package com.example.comp.model.game

import androidx.compose.runtime.mutableStateOf
import com.example.comp.model.game.concept.owner.Ownable
import com.example.comp.model.game.concept.owner.Owner

// Game board spaces that can hold a tile
class LetterBoardSocketModel(val gm: GameBoardModel,
                             val row: Int, val col: Int //for debugging
) : LetterSocketModel() {
    // burned tiles become inaccessible (game rules)
    var burned = mutableStateOf(false)


    override fun accept(tileModel: Ownable<LetterTileModel>): Boolean {
        super.accept(tileModel)
        gm.onTilePlaced(this)
        return true //TODO
    }
}