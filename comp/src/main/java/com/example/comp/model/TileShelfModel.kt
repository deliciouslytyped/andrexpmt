package com.example.comp.model

import androidx.compose.runtime.*
import kotlin.math.absoluteValue
import kotlin.random.Random

// Shelf of playable pieces
class TileShelfModel(val maxTiles : Int = 5) : TileOwner {
    var tiles = mutableStateListOf<LetterTileModel>()

    fun addTile(letterTileModel: LetterTileModel) { //TODO enforce limit
        tiles.add(letterTileModel)
    }

    fun addRandomTiles(i: Int) { //TODO distribution sampling
        val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" //TODO builtin for this?
        (0..<i).forEach {
            val letter = letters.get(Random.nextInt().absoluteValue % letters.length).toString()
            addTile(LetterTileModel(letter, owner = this))
        }
    }

    //TODO include give() in these or use = in parent? any way to enforce not accidentlly dropping a tile? (pass a lda?)
    override fun move(newOwner: TileOwner, tileModel: LetterTileModel) {
        tiles.remove(tiles.find { e -> e == tileModel}) //TODO?
        newOwner.accept(tileModel)
    }

    //TODO shouldnt be implemented, raise exception
    override fun accept(tileModel: LetterTileModel) {
        //tiles.add(mutableStateOf(tileModel))
        TODO("should not be implemented")
    }
}