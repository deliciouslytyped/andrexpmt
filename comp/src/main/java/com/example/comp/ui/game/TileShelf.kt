package com.example.comp.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.comp.dnd.DropTarget
import com.example.comp.model.game.LetterTileModel
import com.example.comp.model.game.TileShelfModel
import com.example.comp.ui.theme.game.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.math.absoluteValue
import kotlin.random.Random

private val shelfShelfSize = 10

@Composable
fun TileShelf(modifier: Modifier = Modifier, model: TileShelfModel) {
    val logger = KotlinLogging.logger {}
    DropTarget<LetterTileModel> { isHover, tileModel ->
        if (tileModel != null){ //TODO shelf limit
            LaunchedEffect(key1=isHover, key2 = tileModel) {//TODO probably wrong?
                logger.debug { "launched shelf move ${isHover} ${tileModel} ${model}" }
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
                Spacer(modifier = Modifier.height(overhang.dp-2.dp))
                Box(modifier = Modifier
                    .background(
                        if (isHover) Color.Yellow // TODO debug
                        else Color.Blue)
                    .height(2.dp)
                    .fillMaxWidth()
                )
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
            //TODO this is terrbile
            val sorted = model.tiles.toSortedMap { a, b ->
                logger.trace { "comparing ${a} ${model.tiles[a]!!} ${b} ${model.tiles[b]!!}" }
                model.tiles[a]!!.compareTo(model.tiles[b]!!)
            }
            val spacings = remember(model, model.tiles, model.tiles.count(), sorted.keys) { //TODO wat
                (0..<model.tiles.count()).map { Random.nextInt().absoluteValue % 20 }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                sorted.map { (model, idx) ->
                    //TODO this will accidentally squeeze tiles, make sure its not a problem
                    Spacer(modifier = Modifier.width(spacings[idx].dp))
                    logger.debug { "adding tile to shelf ${model.label}" }
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
