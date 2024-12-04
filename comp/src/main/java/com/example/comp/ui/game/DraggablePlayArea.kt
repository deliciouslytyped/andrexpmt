package com.example.comp.ui.game

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
import com.example.comp.dnd.DraggableScreen
import com.example.comp.model.game.*
import com.example.comp.ui.screens.GameViewModel

//TODO intellij wont move this file for some reason
//TODO on screen debug log
@Composable
fun DraggablePlayArea(viewModel: GameViewModel) {
    var gm = viewModel.gm
    //TODO for some reason putting new game in a launchedeffect(null){...} here would always trigger newgame on rotation
    DraggableScreen(modifier = Modifier.fillMaxSize()) {
        GameOverOverlay(gm) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                        //    .weight(1f)
                    ) {
                        GameBoard(model = gm.board)
                    }
                    Row {
                        DraggableTileStack(stackModel = gm.newTiles)
                        TileShelfSet(gm.shelf1, gm.shelf2, gm.shelf3)
                    }
                    LazyColumn(
                        modifier = Modifier
                            .background(Color.DarkGray)
                            .fillMaxWidth()
                    ) {
                        gm.board.foundWords.value.words.values
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

@Composable
fun GameOverOverlay(gm: GameModel, content: @Composable (() -> Unit)) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if(gm.gameOver.value){
            Column {
                Text(text = "Game Over :(", textAlign = TextAlign.Center)
                Text(text = "No more reachable tiles.", textAlign = TextAlign.Center)
                Row {
                    Button(onClick = {
                        gm.newGame()
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