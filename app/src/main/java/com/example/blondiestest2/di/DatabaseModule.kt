package com.example.blondiestest2.di

import android.content.Context
import androidx.room.Room
import com.example.blondiestest2.data.local.AppDatabase
import com.example.blondiestest2.data.local.DrinkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "blondies-db")
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideDrinkDao(db: AppDatabase): DrinkDao =
        db.drinkDao()
}