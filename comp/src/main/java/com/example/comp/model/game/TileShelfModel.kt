package com.example.comp.model.game

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.comp.model.game.concept.owner.Ownable
import com.example.comp.model.game.concept.owner.Owner

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


    //TODO shouldnt be implemented, raise exception
    override fun accept(tileModel: Ownable<LetterTileModel>): Boolean {
        tiles.add(tileModel.self())
        //TODO("should not be implemented")
        return true //TODO actually check
    }

    override fun canAccept(t: Ownable<LetterTileModel>): Boolean { //TODO lettertilemodel implements ownable so why doesnt this work
        return tiles.count() < maxTiles
    }

    override fun canRelease(ownable: Ownable<LetterTileModel>): Boolean {
        return ownable in tiles
    }

    //TODO include give() in these or use = in parent? any way to enforce not accidentlly dropping a tile? (pass a lda?)

    override fun release(tileModel: Ownable<LetterTileModel>): Boolean {
        return tiles.remove(tiles.find { e -> e == tileModel}) //TODO?
    }

    fun reset() {
        tiles.clear()
    }
}