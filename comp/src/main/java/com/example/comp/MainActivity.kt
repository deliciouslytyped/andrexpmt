package com.example.comp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp.ui.theme.MyApplicationTheme

    class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                DraggablePlayArea()
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