package com.example.comp.ui.screens

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.comp.data.preferencesdatastore.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenModel @Inject constructor(private val themePreferences: ThemePreferences): ViewModel() {
    //TODO sonnet
    val isDarkTheme = themePreferences.isDarkTheme

    suspend fun toggleTheme() = themePreferences.toggleTheme()

}
