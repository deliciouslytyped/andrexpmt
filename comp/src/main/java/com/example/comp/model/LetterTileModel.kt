package com.example.comp.model

import androidx.annotation.CheckResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.comp.dnd.DraggableViewModel

// Movable tile, can be stored on the shelf and accepted by tile sockets on the game board
class LetterTileModel(val label: String,owner: TileOwner) : DraggableViewModel() {
    var isConnected = mutableStateOf(false)

    var _owner by mutableStateOf(owner)
    //TODO note this isnt an owner, it doesnt own itself. (TODO I guess it could?)
    // It just needs to know its parent so we can move it around when dragging and dropping //TODO other way to do this?
    @CheckResult
    fun move(newOwner: TileOwner): Boolean { //TODO this code is too distributed
        if(!newOwner.canAccept(this)) { return false }
        _owner.move(newOwner, this)
        _owner = newOwner
        return true
    }
}
