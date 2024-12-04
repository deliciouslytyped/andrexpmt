package com.example.comp.model

//TODO none of this is really lazy at this point

typealias Coord = Pair<Int, Int>
class LazyGridModel<T>(val data: List<T>, val cols: Int){ // Cols is the index along the short axis
    // NOTE only works with full rows
    val rowCount: Int
        get() {
            return data.size / cols
        }
    operator fun get(row: Int, col: Int): T {
        return data[row*cols+col]
    }

    fun getRow(r: Int): List<T> {
        return (0..<cols).map { this[r, it] }
    }

    fun getBoundedCol(r: Int, c: Int, radius: Int): List<T> {
        return (-radius..radius)
            .map { r + it }
            .filter { 0 <= it && it < data.count() / cols } //Should always be a whole number because we only add whole rows
            .map { this[it, c] }
    }

    fun coordByItem(s: T): Coord { //TODO
        return idxToCoord(data.indexOf(s))
    }

    fun idxToCoord(i: Int): Coord {
        // cross axis, columns
        return Pair(i / cols, i % cols)
    }

    fun rowsAround(e: T, radius: Int): List<Pair<Int, List<T>>> { //TODO check these actually work
        val (_r, _) = coordByItem(e)
        val radii = (-radius..radius)
        return radii
            .map { _r + it }
            .filter { 0 <= it && it < data.count() / cols } //Should always be a whole number because we only add whole rows
            .map { r -> Pair(r, getRow(r)) }
    }

    fun colsAround(e: T, radius: Int): List<Pair<Int,List<T>>> {
        val (r, _) = coordByItem(e)
        return (0..<cols).map { c ->
            Pair(c, getBoundedCol(r, c, radius))
        }
    }

    fun neighbors(sm: T): List<T> {
        val (r, c) = coordByItem(sm)
        val neighs = mutableListOf<T>()
        listOf((0 to -1), (0 to 1), (1 to 0), (-1 to 0))
            .map { (_r, _c) -> (r + _r to c + _c) }
            .filter { (r, c) -> 0 <= r && r < rowCount && 0 <= c && c < cols }
            .forEach { (r, c) ->
                neighs.add(this[r, c])
            }
        return neighs
    }
}