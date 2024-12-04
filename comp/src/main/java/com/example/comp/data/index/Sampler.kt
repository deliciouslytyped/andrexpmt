package com.example.comp.data.index

import com.example.comp.data.index.Distribution.sampleLetter
import kotlin.math.absoluteValue
import kotlin.random.Random

val _letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" //TODO builtin for this?
fun sampleLetters(n: Int, callback: ((String) -> Unit)) {
    (0..<n).forEach {
        val letter = sampleLetter()
        callback(letter)
    }
}

fun sampleLetters(n: Int): List<String> {
    val letters = ArrayList<String>(n)
    (0..<n).forEach {
        letters.add(sampleLetter())
    }
    return letters
}

fun oldSampleLetter(): String{
    return _letters.get(Random.nextInt().absoluteValue % _letters.length).toString()
}

//TODO too sleep deprived to check if this works
object Distribution {
    val cumulativeFrequencies = mutableListOf<Pair<Char, Double>>()
    var currentSum = 0.0

    //TODO fake chatgpt numbers
    /*val letterFrequencies = mapOf(
        'a' to 8.2, 'b' to 1.5, 'c' to 2.8, 'd' to 4.3, 'e' to 12.7,
        'f' to 2.2, 'g' to 2.0, 'h' to 6.1, 'i' to 7.0, 'j' to 0.2,
        'k' to 0.8, 'l' to 4.0, 'm' to 2.4, 'n' to 6.7, 'o' to 7.5,
        'p' to 1.9, 'q' to 0.1, 'r' to 6.0, 's' to 6.3, 't' to 9.1,
        'u' to 2.8, 'v' to 1.0, 'w' to 2.4, 'x' to 0.2, 'y' to 2.0,
        'z' to 0.1
    )*/
    val scrabbleTileDistribution = mapOf(
        //' ' to 2, // 2 blank tiles //TODO search algo cant handle blanks nicely
        'E' to 12, 'A' to 9, 'I' to 9, 'O' to 8, 'N' to 6, 'R' to 6, 'T' to 6,
        'L' to 4, 'S' to 4, 'U' to 4, 'D' to 4, 'G' to 3, 'B' to 2, 'C' to 2,
        'M' to 2, 'P' to 2, 'F' to 2, 'H' to 2, 'V' to 2, 'W' to 2, 'Y' to 2,
        'K' to 1, 'J' to 1, 'X' to 1, 'Q' to 1, 'Z' to 1
    )


    init {
        for ((letter, freq) in scrabbleTileDistribution) {
            currentSum += freq
            cumulativeFrequencies.add(letter to currentSum)
        }
    }

    fun sampleLetter(): String {
        val randomValue = Random.nextDouble(currentSum)
        return cumulativeFrequencies.first { it.second >= randomValue }.first.uppercase()
    }
}

