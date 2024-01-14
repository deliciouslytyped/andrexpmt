package com.example.comp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp.dnd.DraggableScreen
import com.example.comp.model.GameBoardModel
import com.example.comp.model.IncomingStack
import com.example.comp.model.LetterTileModel
import com.example.comp.model.TileShelfModel
import com.example.comp.model.index.Distribution.sampleLetter
import com.example.comp.presentation.GameBoard
import com.example.comp.presentation.TileShelfSet
import com.example.comp.ui.util.LocalVisualLogger

//TODO intellij wont move this file for some reason
//TODO on screen debug log
@Composable
fun DraggablePlayArea() {
    //TODO
    val stackModel by remember { mutableStateOf(IncomingStack()) }
    val boardModel by remember { mutableStateOf(GameBoardModel(stack = stackModel)) }
    val model1 by remember { mutableStateOf(TileShelfModel()) }
    val model2 by remember { mutableStateOf(TileShelfModel()) }
    val model3 by remember { mutableStateOf(TileShelfModel()) }
    val logger = LocalVisualLogger.current
    //init game
    LaunchedEffect(null) {
        initNewGame(boardModel, stackModel, model1, model2, model3)
    }
    DraggableScreen(modifier = Modifier.fillMaxSize()) {
        GameOverOverlay(boardModel, stackModel, model1, model2, model3) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                        //    .weight(1f)
                    ) {
                        GameBoard(model = boardModel)
                    }
                    Row {
                        DraggableTileStack(stackModel = stackModel)
                        TileShelfSet(model1, model2, model3)
                    }
                    LazyColumn(
                        modifier = Modifier
                            .background(Color.DarkGray)
                            .fillMaxWidth()
                    ) {
                        boardModel.foundWords.value.words.values
                            .let { HashSet(it) }
                            .forEach {
                                item { Text(it) }
                            }
                    }
                }
            }
        }
    }
}

fun initNewGame(
    boardModel: GameBoardModel,
    stackModel: IncomingStack,
    model1: TileShelfModel,
    model2: TileShelfModel,
    model3: TileShelfModel
) {
    //TODO this is crap
    stackModel.reset()
    boardModel.reset()
    model1.reset()
    model2.reset()
    model3.reset()
    (0..10).map { boardModel.addRow() }
    (0..<20).forEach {
        stackModel.addTile(LetterTileModel(sampleLetter(), stackModel))
    }
    (0..<3).forEach {
        stackModel.peek().move(model1)
    }
    (0..<5).forEach {
        stackModel.peek().move(model2)
    }
    (0..<5).forEach {
        stackModel.peek().move(model3)
    }
    boardModel.addStartingTiles()
}

@Preview
@Composable
fun GameOverPreview(){
    val stackModel = remember { IncomingStack() }
    val boardModel by remember { mutableStateOf(GameBoardModel(stack = stackModel)) }
    val model1 by remember { mutableStateOf(TileShelfModel()) }
    val model2 by remember { mutableStateOf(TileShelfModel()) }
    val model3 by remember { mutableStateOf(TileShelfModel()) }
    GameOverOverlay(boardModel = boardModel, stackModel = stackModel, model1 = model1, model2 = model2, model3 = model3) {

    }
}

@Composable
fun GameOverOverlay(boardModel: GameBoardModel, stackModel: IncomingStack, model1: TileShelfModel, model2: TileShelfModel, model3: TileShelfModel, content: @Composable (() -> Unit)) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if(boardModel.gameOver.value){
            Column {
                Text(text = "Game Over :(", textAlign = TextAlign.Center)
                Text(text = "No more reachable tiles.", textAlign = TextAlign.Center)
                Row {
                    Button(onClick = {
                        initNewGame(boardModel = boardModel, stackModel = stackModel, model1 = model1, model2 = model2, model3 = model3)
                    }) {
                        Text(text = "New Game")
                    }
                    Button(onClick = {
                        /*TODO*/
                    }) {
                        Text(text = "Submit Score")
                    }
                    Button(onClick = {
                        /*TODO*/
                    }) {
                        Text(text = "View Scores")
                    }
                }
            }
        }
    }
}

//@Preview
@Composable
fun DraggablePlayAreaPreview(){
    DraggablePlayArea()
}