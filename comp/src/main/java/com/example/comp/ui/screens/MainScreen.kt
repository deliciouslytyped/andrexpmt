package com.example.comp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.comp.data.index.WordTrie
import com.example.comp.ui.theme.MyApplicationTheme
import com.example.comp.ui.theme.ThemeToggle
import com.example.comp.ui.util.logging.LogViewer

@Composable
fun MainScreen(viewModel: MainScreenModel = hiltViewModel()) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())

    Box(modifier = Modifier.fillMaxSize()) { //TODO hack for theme button align
        LogViewer {
            MyApplicationTheme(darkTheme = isDarkTheme) {
                val ctx = LocalContext.current
                val wl = remember { WordTrie.apply { this.loadDictionary(ctx) } }
                /*LaunchedEffect(null){
                    longestExistingSubstring("CAT")
                }
                //logEff("${longestExistingBidirectionalSubstring("CAT")}")*/
                Navigation()
            }
        }
        ThemeToggle(modifier = Modifier.align(Alignment.TopEnd)) //Needs to be on top so we add it last
    }
}