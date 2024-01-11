package com.example.comp.model


// Implements capability to transfer LetterTileModel between owners while only having a reference to the tile and the new owner.
// Used to implement the object passing part of drag and drop.
interface TileOwner {
    fun move(newOwner: TileOwner, tileModel: LetterTileModel)
    fun accept(tileModel: LetterTileModel)
}
