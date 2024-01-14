package com.example.comp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp.dnd.DragTarget
import com.example.comp.dnd.DraggableScreen
import com.example.comp.model.IncomingStack
import com.example.comp.model.LetterTileModel
import com.example.comp.model.index.Distribution.sampleLetter
import com.example.comp.presentation.LetterTile
import com.example.comp.presentation.tileSize
import com.example.comp.ui.theme.game.tileBackground
import com.example.comp.ui.theme.game.tileBorderInner
import com.example.comp.ui.theme.game.tileBorderOuter
import kotlin.math.sqrt

@Composable
fun DraggableTileStack(stackModel: IncomingStack){
    //val topTile = remember(stackModel, stackModel.incomingTiles) { stackModel.incomingTiles.lastOrNull() } // TODO remember?
    val topTile = stackModel.incomingTiles.lastOrNull() // TODO remember?
    Box {
        if(topTile != null){
            DragTarget(dataToDrop = topTile, viewModel = stackModel, dragVisual = {
                LetterTile(model = topTile)
            }) {
                TileStack(stackModel = stackModel)
            }
        } else {
            TileStack(stackModel = stackModel)
        }
    }
}

@Composable
fun TileStack(stackModel: IncomingStack) {
    val tiles = stackModel.count()
    val rotSize = (tileSize * sqrt(2f))
    val tileHeight = tileSize / 7
    val height = tiles*tileHeight + rotSize

    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier
        .width(rotSize.dp)
        .height(height.dp)
    ) {
        //TODO this in a more efficient way (draw the side parallelograms instead of all this overdraw / use images)
        // can use transforms on rectangles for this at least https://stackoverflow.com/questions/69904006/how-to-draw-3d-objects-in-jetpack-compose
        // (or you know, just do this properly with something built for it)
        val limit = if (stackModel.isCurrentlyDragging) tiles - 1 else tiles
        (0..<limit).forEach { i ->
            (0..<tileHeight).forEach { y ->
                translate(rotSize.dp.toPx()/2, (height - rotSize/2 - i * tileHeight - y).dp.toPx()) {
                    rotate(45f, Offset(0f, 0f)) {
                        translate(tileSize.dp.toPx()/-2, tileSize.dp.toPx()/-2) {
                            //drawRect(Color.Red, Offset(0f, 0f), Size(tileSize.dp.toPx(), tileSize.dp.toPx()))
                            drawRect(if (y % tileHeight == 0) Color.DarkGray else tileBorderOuter, topLeft = Offset(0f,0f), size = Size(tileSize.dp.toPx(), tileSize.dp.toPx()))
                            if(i == limit - 1 && y == tileHeight - 1){
                                //LetterTile(model = stackModel.peek())
                                drawRect(tileBackground, Offset(5.dp.toPx(),5.dp.toPx()), Size((tileSize-5).dp.toPx(), (tileSize-5).dp.toPx()))
                                drawRect(tileBorderInner, Offset(1.dp.toPx(),1.dp.toPx()), Size((tileSize-1).dp.toPx(), (tileSize-1).dp.toPx()))
                                translate(25f,-25f) {
                                    drawText(textMeasurer.measure(
                                            stackModel.incomingTiles[i].label,//TODO
                                            style = TextStyle(fontSize = 50.sp) // TODO calculated size?
                                    ))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DraggableTileStackPreview() {
    val model by remember { mutableStateOf(IncomingStack()) }
    (0..<20).forEach {
        model.addTile(LetterTileModel(sampleLetter(), model))
    }
    DraggableScreen {
        DraggableTileStack(model)
    }
}