package com.example.comp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comp.dnd.DraggableScreen
import com.example.comp.dnd.DropItem
import com.example.comp.model.LetterTileModel
import com.example.comp.model.TileShelfModel
import com.example.comp.model.index.Distribution.sampleLetter
import com.example.comp.ui.theme.game.shelfColor
import com.example.comp.ui.theme.game.shelfShelf
import kotlin.math.absoluteValue
import kotlin.random.Random

private val shelfShelfSize = 10

@Composable
fun TileShelf(modifier: Modifier = Modifier, model: TileShelfModel) {
    DropItem<LetterTileModel> {isCurrentDropTarget, tileModel ->
        if (tileModel != null){ //TODO shelf limit
            LaunchedEffect(key1 = isCurrentDropTarget, key2 = tileModel) {
                tileModel.move(model) //TODO figure out how to IOC this or something
            }
        }
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
            //TODO dont quite understand the necessity of keying on tileModel
            //NOTE keying on isCurrentDropTarget isnt needed but it adds a nice interactivity effect
            val spacings = remember(model, model.tiles, model.tiles.count()) { //TODO
                (0..<model.tiles.count()).map { Random.nextInt().absoluteValue % 20 }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                model.tiles.zip(spacings).forEach { (model, spacing) ->
                    //TODO this will accidentally squeeze tiles, make sure its not a problem
                    Spacer(modifier = Modifier.width(spacing.dp))
                    LetterTile(
                        model = model
                    )
                }
            }
        }
    }
}

@Composable
fun TileShelfSet(model1 : TileShelfModel, model2 : TileShelfModel, model3 : TileShelfModel) {
    Column {
        TileShelf(model = model1)
        Spacer(modifier = Modifier.height(20.dp))
        TileShelf(model = model2)
        Spacer(modifier = Modifier.height(20.dp))
        TileShelf(model = model3)
    }
}

@Preview
@Composable
fun TileShelfPreview() {
    val model1 by remember { mutableStateOf(TileShelfModel()) }
    val model2 by remember { mutableStateOf(TileShelfModel()) }
    val model3 by remember { mutableStateOf(TileShelfModel()) }
    LaunchedEffect(null){
        (0..<3).forEach {
            model1.addTile(LetterTileModel(sampleLetter(), model1))
        }
        (0..<5).forEach {
            model2.addTile(LetterTileModel(sampleLetter(), model2))
        }
        (0..<5).forEach {
            model3.addTile(LetterTileModel(sampleLetter(), model2))
        }
    }
    DraggableScreen {
        TileShelfSet(model1, model2, model3)
    }
}

