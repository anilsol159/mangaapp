package com.example.mangaapp.domain.repository

import com.example.mangaapp.domain.model.Manga
import kotlinx.coroutines.flow.Flow

interface MangaRepository {
    suspend fun fetchManga(page: Int): Flow<List<Manga>>
    suspend fun getMangaFromCache(): Flow<List<Manga>>
    suspend fun getMangaById(id: String): Manga?
    suspend fun refreshCache()
}