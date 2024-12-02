package com.example.comp.model.game

import androidx.annotation.CheckResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.comp.dnd.DraggableModel
import com.example.comp.model.game.concept.owner.Ownable
import com.example.comp.model.game.concept.owner.Owner

// Movable tile, can be stored on the shelf and accepted by tile sockets on the game board
class LetterTileModel(val label: String,owner: Owner<LetterTileModel>) : DraggableModel(), Ownable<LetterTileModel> { //TODO owner type "recursion" a little funky?
    var isConnected = mutableStateOf(false)

    var _owner by mutableStateOf(owner)

    override fun getOwner(): Owner<LetterTileModel> {
        return _owner
    }

    override fun setOwner(newOwner: Owner<LetterTileModel>) {
        _owner = newOwner
    }
}
