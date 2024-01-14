package com.example.comp.model.index.cartesian

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp.model.Coord
import com.example.comp.model.index.TrieCursor
import com.example.comp.model.index.WordTrie
import com.example.comp.ui.util.VLogger
import com.example.comp.ui.util.VisualLogger
import com.example.comp.ui.util.VisualLogger.log
import com.example.comp.ui.util.VisualLogger.logAppend
import com.example.comp.ui.util.logEff

//TODO in case of a string readable in both directions, make noteof that somehow for better scoring
fun longestExistingBidirectionalSubstring(s: String): Span? {
    val a = longestExistingSubstring(s)
    val b = longestExistingSubstring(s.reversed())
    /* should be fine?
    a b val
    a>b a
    a<b b
    a~b ~
    n n ~(n)
    n b b
    a n a
     */
    return if ((a?.length ?: 0) > (b?.length ?: 0)) a else b
}

data class Span(val s: String, val startIdx: Int, val endIdx: Int) {
    val length = endIdx - startIdx
}

data class OrientedSpan(val span: Span, val orientation: Char) //TODO

fun longestExistingSubstring(s: String): Span? {
    var s = s + "." // terminator hack
    var maxword: String? = null
    var st: Int? = null
    for (i in 0..<s.length) { //Dont include the empty string //TODO make sure this is correct
        var cursor = TrieCursor()
        //log("${s.substring(i)}")
        //log("${WordTrie.contains(s.substring(i))}")
        for ((j,c) in s.substring(i).withIndex()) {
            logAppend("$c")
            //log("${cursor.valid}")
            cursor[c] //TODO make sure we arent off by one on the depth on this
            logAppend(" ${cursor.valid}")
            //Not sure what I was thinking here but this whole thing is a mess
            if (!cursor.valid && c != '.') { // If we cant pac-man any more characters, check if we can find a terminator and then we've found the longest match for this starter, otherwise we wont find any more for this starter
                                            //i.e. if we cant consume the current character (And the cursor thus didnt step down the tree),
                                            // if instead of the current character, there exists a terminator in the tree at this position
                                            // then we found a matching prefix.    The "c is not terminator" case is separate; if c is a
                                            // terminator then normal matching should find it. (the next branch)
                if ('.' in cursor.position && cursor.depth > (maxword?.length ?: 0) ) {
                    st = i
                    maxword = s.substring(st, st + cursor.depth - 1)
                }
                continue
            } else { // Hitting this branch should mean we've been able to find everything up to this point, includin the . terminator
                if(j == s.length-1 && cursor.valid){//because indexes // since the terminator behavior is tacked-on, had to add a check here that the cursor is valid
                    st = i
                    maxword = s.substring(st, st + cursor.depth - 1)// remember to cut off the terminator
                    //log("--")
                    return Span(maxword, st!!, st + maxword.length - 1)
                }
            }
        }
    }
    //log("--")
    return if (maxword != null) Span(maxword, st!!, st + maxword.length - 1) else null
}

fun findLongestWords(spans: Map<Coord, OrientedSpan>): Map<Coord, OrientedSpan> {
    val res = HashMap<Coord, OrientedSpan>()
    for ((c, s) in spans.entries) {
        val span = longestExistingSubstring(s.span.s) //TODO handle overlapping but noncontained / return multiple spans
        if (span != null) {
            val os = OrientedSpan(Span(span.s, 0, span.s.length), s.orientation)
            if(s.orientation == 'R'){
                res[Coord(c.first+span.startIdx, c.second)] = os //TODO these are probably off by one
            } else if (s.orientation == 'C') {
                res[Coord(c.first, c.second+span.startIdx)] = os
            }
        }
    }
    log(res.map { (k,v) -> "$k -> $v, " }.joinToString())
    return res
}

@Preview
@Composable
fun PreviewRC() {
    VisualLogger.limit = 80
    VLogger {
        logEff("${longestExistingBidirectionalSubstring("CAT")}")
    }
}