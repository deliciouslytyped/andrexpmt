package com.example.comp.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp.ui.game.DraggablePlayArea

@Composable
fun GameScreen() {
    val viewModel: GameViewModel = viewModel()
    DraggablePlayArea(viewModel = viewModel)
}