package com.example.mangaapp.data.remote.dto

data class MangaResponseDto(
    val code: Int,
    val data: List<MangaDto>
)

data class MangaDto(
    val id: String,
    val title: String,
    val sub_title: String,
    val status: String,
    val thumb: String,
    val summary: String,
    val authors: List<String>,
    val genres: List<String>,
    val nsfw: Boolean,
    val type: String,
    val total_chapter: Int,
    val create_at: Long,
    val update_at: Long
)