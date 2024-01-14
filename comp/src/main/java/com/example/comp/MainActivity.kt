//TODO need to start using viewmodels because half my state gets reset on orientation change

package com.example.comp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp.model.index.WordTrie
import com.example.comp.model.index.cartesian.longestExistingBidirectionalSubstring
import com.example.comp.model.index.cartesian.longestExistingSubstring
import com.example.comp.ui.DraggablePlayArea
import com.example.comp.ui.HomeAnimationArea
import com.example.comp.ui.HomeScreen
import com.example.comp.ui.Navigation
import com.example.comp.ui.theme.MyApplicationTheme
import com.example.comp.ui.util.VLogger
import com.example.comp.ui.util.VisualLogger
import com.example.comp.ui.util.logEff

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VLogger {
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