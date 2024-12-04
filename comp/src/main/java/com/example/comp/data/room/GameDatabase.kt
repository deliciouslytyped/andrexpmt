package com.example.comp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//TODO sonnet
@Database(
    entities = [Score::class],
    version = 1
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        fun create(context: Context): GameDatabase {
            return Room.databaseBuilder(
                context,
                GameDatabase::class.java,
                "game_database"
            )
                .fallbackToDestructiveMigration()  // For development only!
                .build()
        }
    }
}