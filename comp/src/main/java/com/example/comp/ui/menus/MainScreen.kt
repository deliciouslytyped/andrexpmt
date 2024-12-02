package com.example.comp.ui.menus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.comp.model.index.WordTrie
import com.example.comp.ui.theme.MyApplicationTheme
import com.example.comp.ui.util.logging.LogViewer

@Composable
fun MainScreen() {
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