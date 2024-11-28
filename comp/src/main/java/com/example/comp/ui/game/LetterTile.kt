package com.example.comp.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp.dnd.DragTarget
import com.example.comp.model.game.LetterBoardSocketModel
import com.example.comp.model.game.LetterTileModel
import com.example.comp.ui.theme.game.*

val tileSize = 50

@Composable
fun LetterTile(modifier: Modifier = Modifier, model: LetterTileModel, draggable: Boolean = true) {
    // Tiles on the game board cant be moved
    if(draggable) {
        DragTarget( //Weird naming, this is what we can drag. DropTarget on LetterSocket is what we can drag it to.
            dataToDrop = model,
            viewModel = model
            ) {
            Content(modifier, model = model)
        }
    } else { Content(modifier, model = model) }
}

@Composable
private fun Content(modifier: Modifier = Modifier, model: LetterTileModel) {
    Box(
        modifier
            .size(tileSize.dp)
            .border(3.dp, tileBorderInner)
            .border(1.dp, tileBorderOuter)
            .background(
                if(model._owner is LetterBoardSocketModel)
                    (if(model.isConnected.value) tileBackground else disconnectedColor)
                else tileBackground
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = model.label,
            fontSize = 30.sp)
    }
}
