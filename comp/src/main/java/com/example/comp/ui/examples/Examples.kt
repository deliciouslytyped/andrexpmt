package com.example.comp.ui.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp.dnd.DraggableScreen
import com.example.comp.model.game.*
import com.example.comp.model.index.Distribution
import com.example.comp.model.index.WordTrie
import com.example.comp.model.index.cartesian.longestExistingBidirectionalSubstring
import com.example.comp.ui.game.*
import com.example.comp.ui.menus.HomeAnimationArea
import com.example.comp.ui.menus.Navigation
import com.example.comp.ui.theme.MyApplicationTheme
import com.example.comp.ui.util.logging.LogViewer
import com.example.comp.ui.util.logging.LogModel
import com.example.comp.ui.util.logging.logEff

@Preview
@Composable
fun LetterTilePreview(){
    val owner = LetterSocketModel()
    LetterTile(model = LetterTileModel("A", owner = owner))
}

@Preview
@Composable
fun DraggableTileStackPreview() {
    val model by remember { mutableStateOf(IncomingStack()) }
    (0..<20).forEach {
        model.addTile(LetterTileModel(Distribution.sampleLetter(), model))
    }
    DraggableScreen {
        DraggableTileStack(model)
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
            model1.addTile(LetterTileModel(Distribution.sampleLetter(), model1))
        }
        (0..<5).forEach {
            model2.addTile(LetterTileModel(Distribution.sampleLetter(), model2))
        }
        (0..<5).forEach {
            model3.addTile(LetterTileModel(Distribution.sampleLetter(), model2))
        }
    }
    DraggableScreen {
        TileShelfSet(model1, model2, model3)
    }
}

@Preview
@Composable
fun LetterSocketPreviewEmpty() {
    val model = LetterSocketModel()
    LetterSocket(model = model)
}

@Preview
@Composable
fun LetterSocketPreviewWithLabel() {
    val model = LetterSocketModel()
    model.label = "+1"
    LetterSocket(model = model)
}

@Preview
@Composable
fun LetterSocketPreviewWithTile() {
    val model = LetterSocketModel()
    model.label = "+1"
    model.tile = LetterTileModel("A", owner = model)
    LetterSocket(model = model)
}

@Preview
@Composable
fun PreviewGameBoard(){
    LogViewer {
        Box {
            val boardModel by remember { mutableStateOf(GameBoardModel(stack = IncomingStack()).apply {
                repeat(20) {
                    this.addRow()
                }
                this.foundWords.value.add(Rect(0f, 0f, 2f, 2f), "test1")
                this.foundWords.value.add(Rect(3f, 3f, 1f, 1f), "test2")
                burnFrontier.value = 2
            }) }
            GameBoard(model = boardModel)
        }
    }

}

@Preview
@Composable
fun HomeAnimationAreaPreview(){
    HomeAnimationArea()
}

@Preview
@Composable
fun GameOverPreview(){
    val stackModel = remember { IncomingStack() }
    val boardModel by remember { mutableStateOf(GameBoardModel(stack = stackModel)) }
    val model1 by remember { mutableStateOf(TileShelfModel()) }
    val model2 by remember { mutableStateOf(TileShelfModel()) }
    val model3 by remember { mutableStateOf(TileShelfModel()) }
    GameOverOverlay(boardModel = boardModel, stackModel = stackModel, model1 = model1, model2 = model2, model3 = model3) {

    }
}

@Preview
@Composable
fun VLoggerPreview() { //TODO handle lazy column
    LogViewer {
        LogModel.limit = 5
        (0..20).plus(listOf(20,20,20)).forEach {
            logEff("$it")
        }
    }
}

// Having vloggerpreview above instantiates mutable global state or something, masking an uninitialized vlogger issue?
@Preview
@Composable
fun MainActivityPreview() {
    //TODO figure out dealing with landscape?
    //val act = LocalContext.current as Activity
    //act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    MyApplicationTheme {
        DraggablePlayArea()
    }
}

@Preview
@Composable
fun NavigationPreview() {
    Navigation()
}

@Preview
@Composable
fun MainPreview() {
    LogViewer {
        MyApplicationTheme {
            val ctx = LocalContext.current
            val wl = remember { WordTrie.apply { this.loadDictionary(ctx) } }
            /*LaunchedEffect(null){
                longestExistingSubstring("CAT")
            }
            //logEff("${longestExistingBidirectionalSubstring("CAT")}")*/
            Navigation()
        }
    }
}

//TODO this is probably mean tot  be a unit test in ortho, or rather, it was just a hack
@Preview
@Composable
fun PreviewRC() {
    LogModel.limit = 80
    LogViewer {
        logEff("${longestExistingBidirectionalSubstring("CAT")}")
    }
}

@Preview
@Composable
fun WordListPreview() {
    LogViewer {
        val ctx = LocalContext.current
        val wl = remember { WordTrie.apply { this.loadDictionary(ctx) } }
    }
}