package com.example.comp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comp.dnd.DraggableScreen
import com.example.comp.model.TileShelfModel
import com.example.comp.ui.theme.game.shelfColor
import com.example.comp.ui.theme.game.shelfShelf
import kotlin.math.absoluteValue
import kotlin.random.Random

private val shelfShelfSize = 10

@Composable
fun TileShelf(modifier: Modifier = Modifier, model: TileShelfModel) {
    Box {
        Column(
            modifier = modifier
                .width(((model.maxTiles + 3) * tileSize).dp)
            ) {
            val overhang = 10
            // space for overhang
            Spacer(modifier = Modifier.height(overhang.dp))
            // the tile area //TODO some kind of stack object?
            Box(modifier = modifier
                .background(shelfColor)
                .fillMaxWidth()
                .height((tileSize - overhang).dp))
            // the shelf holding the tiles
            Box(modifier = Modifier
                .height(shelfShelfSize.dp)
                .fillMaxWidth()
                .background(shelfShelf))
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            model.tiles.forEach { model ->
                //TODO is it possible that i cause a list mutation during access here?
                //TODO this will accidentally squeeze tiles, make sure its not a problem
                Spacer(modifier = Modifier.width((Random.nextInt().absoluteValue % 20).dp))
                LetterTile(
                    model = model
                )
            }
        }
    }
}

@Composable
fun TileShelfSet(model1 : TileShelfModel, model2 : TileShelfModel) {
    Column {
        TileShelf(model = model1)
        Spacer(modifier = Modifier.height(20.dp))
        TileShelf(model = model2)
    }
}

@Preview
@Composable
fun TileShelfPreview() {
    val model = TileShelfModel()
    model.addRandomTiles(5)
    val model2 = TileShelfModel()
    model2.addRandomTiles(5)
    DraggableScreen {
        TileShelfSet(model, model2)
    }
}

