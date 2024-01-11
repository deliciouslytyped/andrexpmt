package com.example.comp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp.dnd.DraggableScreen
import com.example.comp.model.GameBoardModel
import com.example.comp.model.TileShelfModel
import com.example.comp.presentation.GameBoard
import com.example.comp.presentation.TileShelfSet

//TODO intellij wont move this file for some reason
//TODO on screen debug log
@Composable
fun DraggablePlayArea() {
    DraggableScreen(modifier = Modifier.fillMaxSize()) {
        //TODO
        val boardModel by remember { mutableStateOf(GameBoardModel()) }
        val model1 by remember { mutableStateOf(TileShelfModel()) }
        val model2 by remember { mutableStateOf(TileShelfModel()) }
        LaunchedEffect(null){
            model1.addRandomTiles(3) //TODO snapshot thing again
            model2.addRandomTiles(5)
        }

        //init game
        LaunchedEffect(null) {
            (0..10).map { boardModel.addRow() }
        }
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                Column(modifier = Modifier
                    .fillMaxWidth()
                //    .weight(1f)
                ) {
                GameBoard(model = boardModel)
                }
                TileShelfSet(model1, model2)
            }
        }
    }
}

@Preview
@Composable
fun DraggablePlayAreaPreview(){
    DraggablePlayArea()
}