package com.example.comp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController){
    Box(modifier = Modifier
        .fillMaxSize()) {
        HomeAnimationArea()
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { navController.navigate(Screen.Game.route) }) {
                Text("New Game :D")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { navController.navigate(Screen.Scores.route) }) {
                Text("Scores >.>")
            }
        }
    }
}