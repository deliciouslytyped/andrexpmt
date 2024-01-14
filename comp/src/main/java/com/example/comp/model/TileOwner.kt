package com.example.comp.model


// Implements capability to transfer LetterTileModel between owners while only having a reference to the tile and the new owner.
// Used to implement the object passing part of drag and drop.
interface TileOwner {
    @Deprecated("TODO, not deprecated, but use move() on tile instead of the owners")
    fun move(newOwner: TileOwner, tileModel: LetterTileModel)
    fun accept(tileModel: LetterTileModel)
    fun canAccept(t: LetterTileModel): Boolean
}
