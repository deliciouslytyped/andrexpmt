package com.example.comp.model.owner

// Implements tracking ownership of "tokens" (i.e. like tiles on the game board.)

// Implements capability to transfer LetterTileModel between owners while only having a reference to the tile and the new owner.
// Used to implement the object passing part of drag and drop.
interface Owner<T> {
    @Deprecated("TODO, not deprecated, but use move() on tile instead of the owners")
    fun move(newOwner: Owner<T>, model: T)
    fun accept(tileModel: T)
    fun canAccept(t: T): Boolean
}