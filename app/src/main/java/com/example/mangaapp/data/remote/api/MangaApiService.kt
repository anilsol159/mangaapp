package com.example.mangaapp.data.remote.api


import com.example.mangaapp.data.remote.dto.MangaResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MangaApiService {
    @GET("manga/fetch")
    suspend fun fetchManga(
        @Query("page") page: Int,
        @Query("genres") genres: String? = null,
        @Query("nsfw") nsfw: Boolean? = null,
        @Query("type") type: String? = "all",
        @Header("x-rapidapi-host") host: String = "mangaverse-api.p.rapidapi.com",
        @Header("x-rapidapi-key") apiKey: String
    ): MangaResponseDto
}