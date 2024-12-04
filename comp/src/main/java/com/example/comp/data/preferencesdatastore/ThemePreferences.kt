//TODO sonnet
package com.example.comp.data.preferencesdatastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

@Singleton
class ThemePreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")

    val isDarkTheme: Flow<Boolean> = context.themeDataStore.data
        .map { preferences ->
            preferences[isDarkThemeKey] ?: false
        }

    suspend fun toggleTheme() {
        context.themeDataStore.edit { preferences ->
            val current = preferences[isDarkThemeKey] ?: false
            preferences[isDarkThemeKey] = !current
        }
    }
}