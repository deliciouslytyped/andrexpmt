package com.example.comp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.comp.dnd.DraggableViewModel

// Movable tile, can be stored on the shelf and accepted by tile sockets on the game board
class LetterTileModel(val label: String, val owner: TileOwner) : DraggableViewModel() {
    var _owner by mutableStateOf(owner)
    //TODO note this isnt an owner, it doesnt own itself. (TODO I guess it could?)
    // It just needs to know its parent so we can move it around when dragging and dropping //TODO other way to do this?
    fun move(newOwner: TileOwner) {
        _owner.move(newOwner, this)
        _owner = newOwner
    }
}
