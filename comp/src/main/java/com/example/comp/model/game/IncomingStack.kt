package com.example.comp.model.game

import androidx.compose.runtime.mutableStateListOf
import com.example.comp.dnd.DraggableModel
import com.example.comp.model.game.concept.owner.Ownable
import com.example.comp.model.game.concept.owner.Owner

class IncomingStack : Owner<LetterTileModel>,DraggableModel() {
    val incomingTiles = mutableStateListOf<LetterTileModel>()
    fun addTile(l : LetterTileModel){
        incomingTiles.add(l)
    }

    @Deprecated("use move instead")
    fun pop(): LetterTileModel {
        val tile = incomingTiles.takeLast(1)[0] //TODO
        incomingTiles.remove(tile)
        return tile
    }

    fun peek(): LetterTileModel {
        return incomingTiles.last()
    }

    fun count(): Int {
        return incomingTiles.count()
    }


    /*fun pull(n: Int){
        addTile(stack.pop(n))
    }*/

    override fun canAccept(t: Ownable<LetterTileModel>): Boolean {
        return false
    }

    override fun accept(tileModel: Ownable<LetterTileModel>): Boolean {
        TODO("Not yet implemented")
    }

    override fun canRelease(ownable: Ownable<LetterTileModel>): Boolean {
        return ownable.self() in incomingTiles
    }

    override fun release(tileModel: Ownable<LetterTileModel>): Boolean {
        return incomingTiles.remove(incomingTiles.find { e -> e == tileModel}) //TODO?
        //TODO so there is *some* game to it
        //addTile(LetterTileModel(sampleLetter(), this))
    }

    fun reset() {
        incomingTiles.clear()
    }
}