//TODO need to start using viewmodels because half my state gets reset on orientation change

package com.example.comp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.comp.model.index.WordTrie
import com.example.comp.ui.menus.Navigation
import com.example.comp.ui.theme.MyApplicationTheme
import com.example.comp.ui.util.logging.LogViewer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
    }
}