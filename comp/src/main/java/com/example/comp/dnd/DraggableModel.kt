package com.example.comp.dnd

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class DraggableModel { //TODO needs to be ported back to viewmodel somehow

    var isCurrentlyDragging by mutableStateOf(false)
        private set

    fun startDragging(){
        isCurrentlyDragging = true
    }
    fun stopDragging(){
        isCurrentlyDragging = false
    }

}