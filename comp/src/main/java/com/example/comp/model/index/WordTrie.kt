package com.example.comp.model.index

import android.content.Context
import android.icu.text.Transliterator.Position
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp.R
import com.example.comp.ui.util.VLogger
import com.example.comp.ui.util.VisualLogger.log
import com.example.comp.ui.util.readTextFileFromResource
import kotlin.math.max

//TODO this is more for fun than efficiency, probably faster to just search the wordlist?
class TrieNode: HashMap<Char, TrieNode?>() {
    override fun toString(): String {
        return "< ${this.toList().joinToString { (k,v) -> "$k -> { $v }," }}>"
    }
}

//TODO dependency injection and architecture
//TODO get a better wordlist

//TODO unsingletonize

//TODO is this even necessary? technically only valid moves should be allowed, which will be whole words?
object WordTrie {
    val trie: TrieNode = TrieNode()
    //TODO probably dont even need the trie
    val depthIndex: Map<Int, List<TrieNode>> = mapOf()
    var longest: Int? = null //TODO lateinit or whatever // We store this here as a limit for the search algo

    fun loadWord(s: String) {
        var node: TrieNode = trie
        for (c in s) {
            if (c !in node) {
                //log("adding $c")
                val new = TrieNode()
                node[c] = new
                node = new
            } else {
                node = node[c]!! //Shown above to be nonnull
            }
        }
    }

    fun loadDictionary(context: Context) {
        readTextFileFromResource(context, R.raw.sowpods).lines().forEach { s ->
            if(s != "") { //Skip the empty word, probably an artefact of loading or something
                loadWord(s + ".") // terminator hack
            }
            longest = max(longest?:0, s.length)
            //log("${trie}")
        }
    }

    operator fun contains(s: String): Boolean {
        val cursor = TrieCursor()
        for(c in s) {
            cursor[c] //TODO make sure we arent off by one on the depth on this
            if (!cursor.valid) {
                return false
            }
        }
        return true
    }
}

class TrieCursor(
    var trie: WordTrie = WordTrie,
    var position: TrieNode = trie.trie,
    var depth: Int = 0,
    var valid: Boolean = true) { //TODO
    operator fun get(c: Char): TrieCursor { //TODO make sure not off by one errors
        if(c in position){
            val nextpos = position[c]
            if(nextpos != null){
                position = nextpos
            } else {
                valid = false
            }
            depth++
        } else {
            valid = false
        }
        return this
    }
}

@Preview
@Composable
fun WordListPreview() {
    VLogger {
        val ctx = LocalContext.current
        val wl = remember { WordTrie.apply { this.loadDictionary(ctx) } }
    }
}