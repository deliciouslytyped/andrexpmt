package com.example.comp.ui.screens

import androidx.lifecycle.ViewModel
import com.example.comp.model.game.GameModel
import io.github.oshai.kotlinlogging.KotlinLogging

//TODO this is the viewmodel for draggableplayarea
class GameViewModel: ViewModel() {
    val logger = KotlinLogging.logger {}
    val gm = GameModel()
    init {
        logger.debug { "new game viewmodel" }
        gm.newGame()
    }
}