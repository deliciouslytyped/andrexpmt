package com.example.comp.model.game

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Rect
import com.example.comp.model.Coord
import com.example.comp.model.LazyGridModel
import com.example.comp.data.index.Distribution.sampleLetter
import com.example.comp.data.index.FoundWords
import com.example.comp.data.index.WordRect
import com.example.comp.data.index.WordTrie
import com.example.comp.data.index.cartesian.OrientedSpan
import com.example.comp.data.index.cartesian.Span
import com.example.comp.data.index.cartesian.findLongestWords
import com.example.comp.data.index.collectRuns
import java.util.*
import kotlin.math.absoluteValue

//TODO word finding logic needs to be rewritten
//TODO handle stacks
//cols is the size orthogonal to the long axis
//TODO really would preffer the tile placed callback to be a construction parameter...?
class GameBoardModel(val cols: Int = 8, val stack: IncomingStack) { // TODO inifinite game board ....
    var sockets = mutableStateListOf<LetterBoardSocketModel>()
    val gv = LazyGridModel(sockets, cols) //TODO mutablestate? shouldnt need it because its derived from one?

    var burnFrontier = mutableStateOf(0)

    var numRows = 0; //Just for debugging right now

    val burnRate = 3

    private var tilePlacedCallback: (() -> Unit)? = null

    fun setTilePlacedCallback(cb: () -> Unit) {
        tilePlacedCallback = cb
    }

    //Indexed by (didnt profile, premature optimization) bounding box to attempt to decrease number of steps needed to search/draw / have an interface  for it
    //TODO change the types or something NOTE the protocol on the rects is tha they contian r,c,w,h not ltrb
    val foundWords = mutableStateOf(FoundWords()) //TODO does this actually need to be a mutable
    operator fun get(col: Int, row: Int) = gv[col, row]

    fun addStartingTiles() {
        gv.getRow(0).forEachIndexed {
            i, sock -> if (i % 3 == 0) sock.tile = LetterTileModel(sampleLetter(), sock).also { it.isConnected.value = true }
        }
    }

    fun addRow(){
        sockets.addAll((0..<cols).map { col ->
            val s = LetterBoardSocketModel(this, numRows, col)
            if( Random().nextDouble() <= 3f/64f) {
                s.label = "+${6 + Random().nextInt().absoluteValue % 8}"
            }
            s
        }) //TODO snapshot based?
        numRows += 1

    }



    fun burnTiles() {
        gv.getRow(burnFrontier.value).forEach { it.burned.value = true }
    }

    fun onTilePlaced(sm: LetterBoardSocketModel){ //TODO can I have this observe the socket list instead somehow? instead of this spaghetti callback hell
        sm.tile!!.isConnected.value = false
        val newlyActiveSockets = activateFlood(sm)
        newlyActiveSockets.forEach {
            activatePower(it)
        }
        //TODO hackish
        tilePlacedCallback?.invoke()

        findWordsWith(sm)
    }

    //TODO I guess this wont handle long revalidation chains
    fun findWordsWith(sm: LetterBoardSocketModel) {
        getAxis(sm, 'R') { e -> e.rowsAround(sm, WordTrie.longest!!) }
            .let {
                val v = findLongestWords(it)
                v
            }
            .forEach {
                val rect = Rect(
                    it.key.first.toFloat(),
                    it.key.second.toFloat(),
                    1f,
                    it.value.span.length.toFloat()
                )
                foundWords.value.add(rect, it.value.span.s)
            }
        getAxis(sm, 'C') { e -> e.colsAround(sm, WordTrie.longest!!) }
            .let {
                val v = findLongestWords(it)
                v
            }
            .forEach {
                val rect = Rect(
                    it.key.second.toFloat(),
                    it.key.first.toFloat(),
                    it.value.span.length.toFloat(),
                    1f
                )
                foundWords.value.add(rect, it.value.span.s)
            }
    }

    private fun activateFlood(sm: LetterBoardSocketModel): Set<LetterBoardSocketModel> {
        val newlyActive = mutableSetOf<LetterBoardSocketModel>()
        gv.neighbors(sm).filter { it.tile != null }.forEach { neigh ->
            val myConnected = sm.tile!!.isConnected
            val neighConnected = neigh.tile!!.isConnected
            if(!myConnected.value && neighConnected.value) {
                myConnected.value = true
                newlyActive.add(sm)
            }
            if (myConnected.value && !neighConnected.value) {
                neighConnected.value = true
                newlyActive.add(neigh)
            }
        }
        return newlyActive + newlyActive.flatMap { activateFlood(it) }
    }

    fun activatePower(sm: LetterBoardSocketModel) {
        if(sm.label != null){
            //Only more tiles right now
            (0..<Integer.parseInt(sm.label)).forEach {
                stack.addTile(LetterTileModel(sampleLetter(), stack))
            }
        }
    }

    //TODO jfc
    fun getAxis(sm: LetterSocketModel, axis: Char, receiver: ((LazyGridModel<LetterBoardSocketModel>) -> List<Pair<Int,List<LetterSocketModel>>>)): Map<Coord, OrientedSpan>{
        return with(gv, receiver).map { (ax, ts) ->
            ts
                // TODO so now that we have a notion of activation/connectivity, things get more complicated, because things may be come more connected over time and this isnt visible in the first iteration...
                .collectRuns { e -> e.tile != null }
                .filter { (_, run) -> run.any { it.tile!!.isConnected.value } } //TODO this wont be good for deeper gameplay, currently we just filter possible runs as runs with at least one active tile, this will be super "buggy"
                .map {
                    Pair(
                        Coord(ax, it.first),
                        OrientedSpan(Span( //Looks ugly af
                            it.second.map{ it.tile!!.label }.joinToString(separator = ""),
                            it.first,
                            it.first + it.second.size
                        ), axis)
                    )
                }
        }
        .flatten()
        .toMap()

    }

    fun getWordsInRange(start: Int, end: Int): List<WordRect>{
        return foundWords.value.getInRange(start,end)
    }

    fun reset() {
        sockets.clear()
        burnFrontier.value = 0
        val f = foundWords.value
        f.clear()
        foundWords.value = f
    }
}