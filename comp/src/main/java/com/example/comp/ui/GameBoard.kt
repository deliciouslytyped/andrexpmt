package com.example.comp.presentation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comp.model.GameBoardModel

// Prevew is in DraggablePlayArea
@Composable
fun GameBoard(modifier: Modifier = Modifier, model: GameBoardModel){
    //TODO why does aspectratio need to be set (why isnt width/height enough) on the box of LetterSocket for this to work right?
    // https://stackoverflow.com/questions/75429233/how-to-adjust-grid-cells-size-to-fill-max-size-in-jetpack-compose
    LazyHorizontalGrid(
        rows = GridCells.Fixed(model.cols),
        modifier = Modifier
            .width((tileSize *10).dp) //TODO
            .height((tileSize *model.cols).dp)) {
        items(model.sockets) {
            socket -> LetterSocket(model = socket)
        }
    }
}