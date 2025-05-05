package com.example.mangaapp.di

import android.content.Context
import com.example.mangaapp.data.local.dao.MangaDao
import com.example.mangaapp.data.local.dao.UserDao
import com.example.mangaapp.data.local.database.AppDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideMangaDao(database: AppDatabase): MangaDao {
        return database.mangaDao()
    }
}