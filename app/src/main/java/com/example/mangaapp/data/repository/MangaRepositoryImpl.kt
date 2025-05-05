package com.example.mangaapp.data.repository

import com.example.mangaapp.data.local.dao.MangaDao
import com.example.mangaapp.data.local.entities.MangaEntity
import com.example.mangaapp.data.remote.api.MangaApiService
import com.example.mangaapp.data.remote.dto.MangaDto
import com.example.mangaapp.domain.model.Manga
import com.example.mangaapp.domain.repository.MangaRepository
import com.example.mangaapp.util.NetworkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
    private val mangaApiService: MangaApiService,
    private val mangaDao: MangaDao,
    private val networkManager: NetworkManager
) : MangaRepository {

    override suspend fun fetchManga(page: Int): Flow<List<Manga>> = flow {
        if (networkManager.isConnected()) {
            try {
                val response = mangaApiService.fetchManga(
                    page = page,
                    apiKey = "0c584917b7msh25246f3e5a52a48p1d12b9jsn5fa6d0ff1c6f"
                )
                if (response.code == 200) {
                    val mangaEntities = response.data.map { it.toEntity() }
                    mangaDao.insertManga(mangaEntities)
                    emit(mangaEntities.map { it.toDomainModel() })
                } else {
                    // Emit cached data if API fails
                    mangaDao.getAllManga().collect { cachedManga ->
                        emit(cachedManga.map { it.toDomainModel() })
                    }
                }
            } catch (e: Exception) {
                // Emit cached data if network error
                mangaDao.getAllManga().collect { cachedManga ->
                    emit(cachedManga.map { it.toDomainModel() })
                }
            }
        } else {
            // No network, get from cache
            mangaDao.getAllManga().collect { cachedManga ->
                emit(cachedManga.map { it.toDomainModel() })
            }
        }
    }

    override suspend fun getMangaFromCache(): Flow<List<Manga>> {
        return mangaDao.getAllManga().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getMangaById(id: String): Manga? {
        return mangaDao.getMangaById(id)?.toDomainModel()
    }

    override suspend fun refreshCache() {
        if (networkManager.isConnected()) {
            try {
                val response = mangaApiService.fetchManga(
                    page = 1,
                    apiKey = "0c584917b7msh25246f3e5a52a48p1d12b9jsn5fa6d0ff1c6f"
                )
                if (response.code == 200) {
                    mangaDao.clearManga()
                    val mangaEntities = response.data.map { it.toEntity() }
                    mangaDao.insertManga(mangaEntities)
                }
            } catch (e: Exception) {
                // Handle error silently for refresh
            }
        }
    }
}

// Extension functions for mapping
fun MangaDto.toEntity(): MangaEntity {
    return MangaEntity(
        id = id,
        title = title,
        subTitle = sub_title,
        status = status,
        thumb = thumb,
        summary = summary,
        authors = authors,
        genres = genres,
        nsfw = nsfw,
        type = type,
        totalChapter = total_chapter,
        createAt = create_at,
        updateAt = update_at
    )
}

fun MangaEntity.toDomainModel(): Manga {
    return Manga(
        id = id,
        title = title,
        subTitle = subTitle,
        status = status,
        thumb = thumb,
        summary = summary,
        authors = authors,
        genres = genres,
        nsfw = nsfw,
        type = type,
        totalChapter = totalChapter,
        createAt = createAt,
        updateAt = updateAt
    )
}