package com.example.comp.model.game

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.comp.model.owner.Owner

// Shelf of playable pieces
class TileShelfModel(val maxTiles : Int = 5) : Owner<LetterTileModel>, ViewModel() {
    var tiles = mutableStateListOf<LetterTileModel>()

    @Deprecated("use move instead")
    fun addTile(letterTileModel: LetterTileModel) { //TODO enforce limit
        tiles.add(letterTileModel)
    }

    //TODO move elsewhere, dont need anymore
    /*fun addRandomTiles(i: Int) { //TODO distribution sampling
        sampleLetters(i) {letter ->
            addTile(LetterTileModel(letter, owner = this))
        }
    }*/

    //TODO include give() in these or use = in parent? any way to enforce not accidentlly dropping a tile? (pass a lda?)
    override fun move(newOwner: Owner<LetterTileModel>, tileModel: LetterTileModel) {
        tiles.remove(tiles.find { e -> e == tileModel}) //TODO?
        newOwner.accept(tileModel)
    }

    //TODO shouldnt be implemented, raise exception
    override fun accept(tileModel: LetterTileModel) {
        tiles.add(tileModel)
        //TODO("should not be implemented")
    }

    override fun canAccept(t: LetterTileModel): Boolean {
        return tiles.count() < maxTiles
    }

    fun reset() {
        tiles.clear()
    }
}