package com.example.comp.model

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

//TODO handle stacks
//cols is the size orthogonal to the long axis
class GameBoardModel(val cols: Int = 7) : ViewModel() {
    var sockets = mutableStateListOf<LetterSocketModel>()

    fun addRow(){
        sockets.addAll((0..<cols).map { _ -> LetterSocketModel() }) //TODO snapshot based?
    }
}