package com.example.comp.model.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.comp.model.owner.Owner

// Game board spaces that can hold a tile
open class LetterSocketModel : ViewModel(), Owner<LetterTileModel> {
    var tile by mutableStateOf<LetterTileModel?>(null)
    var label by mutableStateOf<String?>(null)
    override fun canAccept(t: LetterTileModel): Boolean {
        return tile == null
    }

    override fun move(newOwner: Owner<LetterTileModel>, tileModel: LetterTileModel) { //TODO make alias type for this owner type?
        TODO("Not yet implemented") //TODO shouldnt be impemented
    }

    override fun accept(tileModel: LetterTileModel) {
        tile = tileModel
    }
}