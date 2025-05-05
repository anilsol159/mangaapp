package com.example.mangaapp.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.mangaapp.data.local.converters.StringListConverter
import com.example.mangaapp.data.local.dao.MangaDao
import com.example.mangaapp.data.local.dao.UserDao
import com.example.mangaapp.data.local.entities.MangaEntity
import com.example.mangaapp.data.local.entities.UserEntity


@Database(
    entities = [UserEntity::class, MangaEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // For database version updates
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}