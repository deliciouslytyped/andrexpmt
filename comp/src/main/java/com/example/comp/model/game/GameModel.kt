package com.example.comp.model.game

import androidx.compose.runtime.mutableStateOf
import com.example.comp.model.game.concept.GameModel
import com.example.comp.data.index.Distribution
import io.github.oshai.kotlinlogging.KotlinLogging

class GameModel( //TODO this stuff overall seems to be "state holder pattern", downside is we end up integrating compose state? (mutabelstateof and such is just kotlin no?)
//TODO make these parametric / injectable / something
// and a list of game elements
    val newTiles: IncomingStack = IncomingStack(),
    val board: GameBoardModel = GameBoardModel(stack = newTiles),
    val shelf1: TileShelfModel = TileShelfModel(),
    val shelf2: TileShelfModel = TileShelfModel(),
    val shelf3: TileShelfModel = TileShelfModel()
) : GameModel {

    var turnCounter = mutableStateOf(0)
    var gameOver = mutableStateOf(false)

    init {
        board.setTilePlacedCallback { turnLogic() }
    }
    override fun newGame() {
        val logger = KotlinLogging.logger {}
        logger.debug { "new game" }
        //TODO this is crap
        newTiles.reset()
        board.reset()
        shelf1.reset()
        shelf2.reset()
        shelf3.reset()
        reset()
        (0..10).map { board.addRow() }
        (0..<20).forEach {
            newTiles.addTile(LetterTileModel(Distribution.sampleLetter(), newTiles))
        }
        (0..<3).forEach {
            newTiles.peek().move(shelf1)
        }
        (0..<5).forEach {
            newTiles.peek().move(shelf2)
        }
        (0..<5).forEach {
            newTiles.peek().move(shelf3)
        }
        board.addStartingTiles()
    }

    fun reset() {
        turnCounter.value = 0
        gameOver.value = false
    }

    fun turnLogic(){ // rename to turn based game?
        turnCounter.value++
        if(turnCounter.value % board.burnRate == 0){
            board.addRow()
            board.burnTiles() // TODO need because of drag and drop
            val isGameOver = checkGameOver()
            if (isGameOver) {
                gameOver.value = true
            }
            board.burnFrontier.value++
        }
    }

    // Game over if no more reachable active tiles
    override fun checkGameOver(): Boolean { //TODO probably refactor
        return !((board.burnFrontier.value)..<board.gv.rowCount) //TODO why didnt this crash when it was just ..
            .any { board.gv.getRow(it).any { it.tile?.isConnected?.value == true } }
    }


}