package com.example.comp.di

import android.content.Context
import com.example.comp.data.room.GameDatabase
import com.example.comp.data.room.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    @Provides
    @Singleton //TODO do I really need to specify this?
    fun provideDatabase(@ApplicationContext context: Context): GameDatabase { //TODO correct context?
        return GameDatabase.create(context)
    }

    @Provides
    fun provideScoreDao(database: GameDatabase): ScoreDao {
        return database.scoreDao()
    }
}