package com.example.comp.ui.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScoreScreen() {
    val vm: ScoreViewModel = hiltViewModel()
    ScoreDisplayArea(viewModel = vm)
}