package com.example.comp.model.game

import androidx.compose.runtime.mutableStateListOf
import com.example.comp.dnd.DraggableViewModel
import com.example.comp.model.owner.Owner

class IncomingStack : Owner<LetterTileModel>,DraggableViewModel() {
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

    override fun move(newOwner: Owner<LetterTileModel>, tileModel: LetterTileModel) {
        incomingTiles.remove(incomingTiles.find { e -> e == tileModel}) //TODO?
        newOwner.accept(tileModel)
        //TODO so there is *some* game to it
        //addTile(LetterTileModel(sampleLetter(), this))
    }

    override fun canAccept(t: LetterTileModel): Boolean {
        return true
    }

    override fun accept(tileModel: LetterTileModel) {
        TODO("Not yet implemented")
    }

    fun reset() {
        incomingTiles.clear()
    }
}