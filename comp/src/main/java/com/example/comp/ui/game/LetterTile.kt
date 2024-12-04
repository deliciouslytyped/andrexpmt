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
import com.example.comp.dnd.DraggableComposable
import com.example.comp.model.game.LetterBoardSocketModel
import com.example.comp.model.game.LetterTileModel
import com.example.comp.ui.theme.game.*
import io.github.oshai.kotlinlogging.KotlinLogging

val tileSize = 50

@Composable
fun LetterTile(modifier: Modifier = Modifier, model: LetterTileModel, draggable: Boolean = true) {
    //val content = { Content(modifier, model = model) // TODO interestingly, leads to two different behaviours if this is here or duplicated in the if branches... / completely breaks
    // Tiles on the game board cant be moved
    val logger = KotlinLogging.logger {  }
    logger.trace {"recomposing lettertile with $draggable ${model.label}"}

    if(draggable) {
        DraggableComposable( //Weird naming, this is what we can drag. DropTarget on LetterSocket is what we can drag it to.
            dataToDrop = model,
            model = model
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
