//TODO sonnet
package com.example.comp.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.comp.R
import com.example.comp.data.preferencesdatastore.ThemePreferences
import kotlinx.coroutines.launch

@Composable
fun ThemeToggle(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val themePreferences = ThemePreferences(context)
    val isDarkTheme by themePreferences.isDarkTheme.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    IconButton(
        modifier = modifier,
        onClick = {
            scope.launch {
                themePreferences.toggleTheme()
            }
        }
    ) {
        Icon(
            painter = painterResource(
                if (isDarkTheme) R.drawable.ic_light
                else R.drawable.ic_dark
            ),
            contentDescription = if (isDarkTheme) "Switch to light theme" else "Switch to dark theme"
        )
    }
}