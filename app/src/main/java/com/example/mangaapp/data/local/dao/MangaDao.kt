package com.example.mangaapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mangaapp.data.local.entities.MangaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MangaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManga(manga: List<MangaEntity>)

    @Query("SELECT * FROM manga ORDER BY updateAt DESC")
    fun getAllManga(): Flow<List<MangaEntity>>

    @Query("SELECT * FROM manga WHERE id = :id")
    suspend fun getMangaById(id: String): MangaEntity?

    @Query("DELETE FROM manga")
    suspend fun clearManga()
}