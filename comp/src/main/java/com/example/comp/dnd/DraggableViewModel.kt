package com.example.comp.dnd

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

open class DraggableViewModel :ViewModel() {

    var isCurrentlyDragging by mutableStateOf(false)
        private set

    fun startDragging(){
        isCurrentlyDragging = true
    }
    fun stopDragging(){
        isCurrentlyDragging = false
    }

}