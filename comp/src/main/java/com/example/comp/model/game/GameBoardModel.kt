package com.example.comp.model.game

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import com.example.comp.model.Coord
import com.example.comp.model.LazyGridView
import com.example.comp.model.index.Distribution.sampleLetter
import com.example.comp.model.index.WordTrie
import com.example.comp.model.index.cartesian.OrientedSpan
import com.example.comp.model.index.cartesian.Span
import com.example.comp.model.index.cartesian.findLongestWords
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.absoluteValue

//TODO word finding logic needs to be rewritten
//TODO handle stacks
//cols is the size orthogonal to the long axis
class GameBoardModel(val cols: Int = 8, val stack: IncomingStack) {
    var sockets = mutableStateListOf<LetterBoardSocketModel>()
    val gv = LazyGridView(sockets, cols) //TODO mutablestate? shouldnt need it because its derived from one?

    var turnCounter = mutableStateOf(0)
    var burnFrontier = mutableStateOf(0)
    var gameOver = mutableStateOf(false)


    val burnRate = 3

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
        sockets.addAll((0..<cols).map { _ ->
            val s = LetterBoardSocketModel(this)
            if( Random().nextDouble() <= 3f/64f) {
                s.label = "+${6 + Random().nextInt().absoluteValue % 8}"
            }
            s
        }) //TODO snapshot based?

    }

    fun turnLogic(){
        turnCounter.value++
        if(turnCounter.value % burnRate == 0){
            addRow()
            burnTiles() // TODO need because of drag and drop
            val isGameOver = checkGameOver()
            if (isGameOver) {
                gameOver.value = true
            }
            burnFrontier.value++
        }
    }

    // Game over if no more reachable active tiles
    fun checkGameOver(): Boolean {
        return !((burnFrontier.value)..<gv.rowCount) //TODO why didnt this crash when it was just ..
            .any { gv.getRow(it).any { it.tile?.isConnected?.value == true } }
    }

    private fun burnTiles() {
        gv.getRow(burnFrontier.value).forEach { it.burned.value = true }
    }

    fun onTilePlaced(sm: LetterBoardSocketModel){ //TODO can I have this observe the socket list instead somehow? instead of this spaghetti callback hell
        sm.tile!!.isConnected.value = false
        val newlyActiveSockets = activateFlood(sm)
        newlyActiveSockets.forEach {
            activatePower(it)
        }
        turnLogic()

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
    fun getAxis(sm: LetterSocketModel, axis: Char, receiver: ((LazyGridView<LetterBoardSocketModel>) -> List<Pair<Int,List<LetterSocketModel>>>)): Map<Coord, OrientedSpan>{
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
        turnCounter.value = 0
        burnFrontier.value = 0
        gameOver.value = false
        val f = foundWords.value
        f.clear()
        foundWords.value = f
    }
}

fun <T> List<T>.collectRuns(p: ((T) -> Boolean)): List<Pair<Int, List<T>>> {
    return this.foldIndexed(mutableListOf<Pair<Int,MutableList<T>>>()) { i, acc, t ->
        if(p(t)) {
            if(acc.isEmpty()){
                acc.add(Pair(i,mutableListOf(t)))
            } else {
                acc.last().second.add(t)
            }
        } else if (acc.isNotEmpty() && acc.last().second.isNotEmpty()) {
            acc.add(Pair(i, mutableListOf()))
        }
        acc
    }.dropLastWhile { it.second.isEmpty() } //TODO not sure how to do it without possibly leaving an extra list at the end
}

class FoundWords { //TODO test this
    var leftEndIndex = WordEndRangeIndex() //TODO
    var rightEndIndex = WordEndRangeIndex() //TODO
    var words = mutableStateMapOf<WordRect, String>()

    //TODO the search algorithm is probably doing a lot of extra work with nonincremental searches and readding everything
    fun add(r: WordRect, s: String){
        leftEndIndex.add(r.left.toInt(), r)
        rightEndIndex.add(r.right.toInt(), r)
        words.put(r, s)
    }

    fun clear(){
        words.clear()
        leftEndIndex.clear()
        rightEndIndex.clear()
    }
    fun getInRange(start: Int, end: Int): List<WordRect> {
        //TODO mutableSetOf?
        //TODO this looks pretty heavy
        return HashSet(leftEndIndex.getInRange(start, end))
            .union(HashSet(rightEndIndex.getInRange(start, end)))
            .toList()
    }
}

// We implement this to provide some amount of encapsulation for the mutablestate we have to handle manually here
typealias FoundKey = Int
typealias FoundValue = WordRect
typealias FoundWordIndex = TreeMap<FoundKey, HashSet<FoundValue>>
typealias WordRect = Rect //TODO
class WordEndRangeIndex {
    private var foundWords = mutableStateOf(FoundWordIndex()) //We have to use a hacky update function to make sure this works

    fun add(endIndex: FoundKey, value: FoundValue) { //TODO inefficient
        val item = foundWords.value[endIndex]
        if (item != null){
            item.add(value)
        } else {
            val newItem = HashSet<FoundValue>()
            newItem.add(value)
            foundWords.value[endIndex] = newItem
        }
        foundWords.value = TreeMap(foundWords.value)
    }

    fun clear(){
        val f = foundWords.value
        f.clear()
        foundWords.value = f
    }

    //TODO can I somehow return some read only object?
    fun read(): FoundWordIndex {
        return foundWords.value
    }

    fun getInRange(start: Int, end: Int): Set<WordRect> {
        return read().subMap(start, true, end, true).values.toList().flatten().toSet() //TODO eh
    }
}
