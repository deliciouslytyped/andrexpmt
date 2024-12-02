package com.example.comp.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.comp.model.game.GameBoardModel
import com.example.comp.model.game.IncomingStack
import com.example.comp.model.game.TileShelfModel

//TODO this is the viewmodel for draggableplayarea
class GameViewModel( //TODO do these need to be stateflows or...? //TODO I guess these should come from/initialized from repository?...or?
    private val _stackModel: IncomingStack, //TODO need to create a DI module for these i guess
    private val _boardModel: GameBoardModel,
    private val _shelf1: TileShelfModel,
    private val _shelf2: TileShelfModel,
    private val _shelf3: TileShelfModel
) : ViewModel() {

}

/*
//TODO
val stackModel by remember { mutableStateOf(IncomingStack()) }
val boardModel by remember { mutableStateOf(GameBoardModel(stack = stackModel)) }
val model1 by remember { mutableStateOf(TileShelfModel()) }
val model2 by remember { mutableStateOf(TileShelfModel()) }
val model3 by remember { mutableStateOf(TileShelfModel()) }
//val logger = LocalVisualLogger.current
 */