package com.example.comp.model.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.comp.model.game.concept.owner.Ownable
import com.example.comp.model.game.concept.owner.Owner
import io.github.oshai.kotlinlogging.KotlinLogging

// Shelf of playable pieces
class TileShelfModel(val maxTiles : Int = 5) : Owner<LetterTileModel>, ViewModel() {
    // Having this be a list means we get the nice "drop back in randomize" effect, this is more complicated...
    private var _tiles = mutableStateMapOf<LetterTileModel, Int>()
    var tiles: Map<LetterTileModel, Int> = _tiles // read-only

    @Deprecated("use move instead")
    fun addTile(letterTileModel: LetterTileModel) { //TODO enforce limit
        _tiles[letterTileModel]=_tiles.count()
    }

    //TODO move elsewhere, dont need anymore
    /*fun addRandomTiles(i: Int) { //TODO distribution sampling
        sampleLetters(i) {letter ->
            addTile(LetterTileModel(letter, owner = this))
        }
    }*/


    //TODO shouldnt be implemented, raise exception
    override fun accept(tileModel: Ownable<LetterTileModel>): Boolean {
        val idx = _tiles.count()
        _tiles[tileModel.self()]=idx
        //TODO("should not be implemented")
        var logger = KotlinLogging.logger {}
        logger.debug { "accept ${tileModel.self().label} at $idx $tileModel from $this" } // -1 because we've adedd it at this point
        return true //TODO actually check
    }

    override fun canAccept(t: Ownable<LetterTileModel>): Boolean { //TODO lettertilemodel implements ownable so why doesnt this work
        return (tiles.count() < maxTiles) or (t in tiles)
    }

    override fun canRelease(ownable: Ownable<LetterTileModel>): Boolean {
        return ownable in tiles
    }

    //TODO include give() in these or use = in parent? any way to enforce not accidentlly dropping a tile? (pass a lda?)

    override fun release(tileModel: Ownable<LetterTileModel>): Boolean { //NOTE need to renumber indexes
        val idx = tiles.keys.indexOf(tileModel)
        val innerIdx = tiles[tileModel]!!
        val res = _tiles.remove(tiles.keys.find { e -> e == tileModel}) //TODO?
        var logger = KotlinLogging.logger {}
        logger.debug { "remove $res ${tileModel.self().label} at $idx $tileModel from $this" }
        if(res != null) {
            _tiles.entries.forEach { (k, v) ->
                if (v > innerIdx) {
                    _tiles[k] = _tiles[k]!! - 1
                }
            }
        }
        return res != null
    }

    fun reset() {
        _tiles.clear()
    }
}