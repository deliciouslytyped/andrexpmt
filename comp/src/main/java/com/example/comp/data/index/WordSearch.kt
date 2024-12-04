package com.example.comp.data.index

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Rect
import java.util.*
import kotlin.collections.HashSet

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
