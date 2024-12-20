package com.example.comp.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.comp.model.game.GameBoardModel
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

// Prevew is in DraggablePlayArea
@Composable
fun GameBoard(modifier: Modifier = Modifier, model: GameBoardModel, lgState : LazyGridState = rememberLazyGridState()) {
    //TODO why does aspectratio need to be set (why isnt width/height enough) on the box of LetterSocket for this to work right?
    // https://stackoverflow.com/questions/75429233/how-to-adjust-grid-cells-size-to-fill-max-size-in-jetpack-compose
    Box {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(model.cols),
            modifier = Modifier
                .width((tileSize * 10).dp) //TODO
                .height((tileSize * model.cols).dp),
            state = lgState
            ) {
            items(model.sockets) {
                socket -> LetterBoardSocket(model = socket)
            }
        }
        FoundWordOverlay(gridState = lgState, model = model)
    }
}

@Composable
fun BurnOverlay(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color(0x95FF5900)))
}

//needs: scroll position
//       clipping box
@Composable
fun FoundWordOverlay(gridState : LazyGridState, model: GameBoardModel){
//TODO this seems like it would be a lot harder under any different circumstances
    val xOffset = with(LocalDensity.current){
        //log("${gridState.firstVisibleItemIndex} ${gridState.firstVisibleItemScrollOffset} ${gridState.firstVisibleItemIndex / model.cols} ${(gridState.firstVisibleItemIndex / model.cols) * ((tileSize).dp).toPx()}")
        (
        - gridState.firstVisibleItemScrollOffset
                //NOTE/TODO this snippet is commented out because its included in the it.left - start calculation below.
                // TODO this assumes a homogeneous grid, can the model account for others?
                //- (gridState.firstVisibleItemIndex / model.cols) * ((tileSize).dp).toPx()
        ) }
    val tileSizePx = with(LocalDensity.current){ tileSize.dp.toPx()}
    val start = gridState.firstVisibleItemIndex / model.cols
    BoxWithConstraints {
        Canvas(modifier = Modifier
            //.fillMaxSize()
        ) {

            //TODO doc says using gridstate.layout can lead to infinite recomposition so currently hacking in a hardcoded value
            model.getWordsInRange(start, start+9).apply { logger.info("${this.size}rs ") }.forEach {
                drawRect(Color(0x55992222),
                    size = Size(it.right * tileSizePx, it.bottom * tileSizePx),
                    topLeft = Offset(xOffset + (it.left - start) * tileSizePx, it.top * tileSizePx))
            }
        }
        BurnOverlay(modifier = Modifier
            .offset(with(LocalDensity.current){ xOffset.toDp() })
            .width(((model.burnFrontier.value - start) * tileSize).dp)
            .height((tileSize * model.cols).dp) //TODO hack
        )
    }
}

