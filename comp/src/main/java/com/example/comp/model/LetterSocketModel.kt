package com.example.comp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Game board spaces that can hold a tile
open class LetterSocketModel : ViewModel(), TileOwner {
    var tile by mutableStateOf<LetterTileModel?>(null)
    var label by mutableStateOf<String?>(null)
    override fun canAccept(t: LetterTileModel): Boolean {
        return tile == null
    }

    override fun move(newOwner: TileOwner, tileModel: LetterTileModel) {
        TODO("Not yet implemented") //TODO shouldnt be impemented
    }

    override fun accept(tileModel: LetterTileModel) {
        tile = tileModel
    }
}