package com.example.mangaapp.domain.usecase

import com.example.mangaapp.domain.model.Manga
import com.example.mangaapp.domain.repository.MangaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMangaUseCase @Inject constructor(
    private val mangaRepository: MangaRepository
) {
    suspend fun fetchManga(page: Int): Flow<List<Manga>> {
        return mangaRepository.fetchManga(page)
    }

    suspend fun getCachedManga(): Flow<List<Manga>> {
        return mangaRepository.getMangaFromCache()
    }

    suspend fun getMangaById(id: String): Manga? {
        return mangaRepository.getMangaById(id)
    }
}