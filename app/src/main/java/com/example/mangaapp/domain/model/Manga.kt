package com.example.mangaapp.domain.model


data class Manga(
    val id: String,
    val title: String,
    val subTitle: String,
    val status: String,
    val thumb: String,
    val summary: String,
    val authors: List<String>,
    val genres: List<String>,
    val nsfw: Boolean,
    val type: String,
    val totalChapter: Int,
    val createAt: Long,
    val updateAt: Long
)