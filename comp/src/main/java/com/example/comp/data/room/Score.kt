package com.example.comp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val score: Int
)