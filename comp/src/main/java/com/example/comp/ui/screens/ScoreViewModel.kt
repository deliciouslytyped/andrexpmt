package com.example.comp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comp.data.RetrofitScoreRepository
import com.example.comp.data.RoomScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(val sr: RoomScoreRepository, val remoteSr: RetrofitScoreRepository): ViewModel() {
    val logger = KotlinLogging.logger {}

    //TODO shouldnt unnecessarily update in the background
    val scores = sr.observeScores().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
        )

    //TODO handle timeout and whatever
    val remoteScores = remoteSr.observeScores().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertRoomTestData() {
        viewModelScope.launch {
            sr.insertTestData()
        }
    }
    fun insertRetrofitTestData() {
        viewModelScope.launch {
            remoteSr.insertTestData()
        }
    }

    fun setRemoteURL(s: String): Boolean {
        try {
            remoteSr.setURL(s)
            logger.debug {"Set url to $s"}
        } catch (e: IllegalArgumentException) {
            return false
        }
        return true
    }
}