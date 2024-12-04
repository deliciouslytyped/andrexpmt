package com.example.comp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ScoreDisplayArea(viewModel: ScoreViewModel) {
    val scores by viewModel.scores.collectAsState()
    val remoteScores by viewModel.remoteScores.collectAsState()
    var mode by remember { mutableStateOf(false) } // false = room true = retrofit
    var urlValid by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {//TODO hack for test screenshot, otherwise shrinkfit??
        Button(onClick = { viewModel.insertRoomTestData() }) {
            Text("Insert Room test data")
        }
        Button(onClick = { viewModel.insertRetrofitTestData() }) {
            Text("Insert Retrofit test data")
        }
        Button(onClick = { mode = !mode }) {
            Text("Use ${if(mode) "room" else "retrofit"}")
        }
        Text("Using ${if(!mode) "room" else "retrofit"}")
        if(mode) {
            TextField(
                value = url,
                onValueChange = {
                    url = it
                    urlValid = viewModel.setRemoteURL(it)
                },
                isError = !urlValid,
                modifier = if(!urlValid) Modifier.background(Color.Red) else Modifier)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = rememberLazyGridState()
        ) {
            //TODO correect way to do this? backend needs to be stable or something with this setup probably? TODO what happens if i change the database under it?
            (if(mode) remoteScores else scores).forEach { score ->
                item { Text(score.name) }
                item { Text("${score.score}") }

            }
        }
    }
}

@Preview
@Composable
fun ScoreDisplayPreview() {
    ScoreDisplayArea(viewModel())
}