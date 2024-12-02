//TODO need to start using viewmodels because half my state gets reset on orientation change

package com.example.comp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.comp.ui.menus.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //TODO do I actually need this here?
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}