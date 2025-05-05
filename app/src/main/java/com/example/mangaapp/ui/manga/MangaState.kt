package com.example.mangaapp.ui.manga

import com.example.mangaapp.domain.model.Manga


data class MangaState(
    val mangaList: List<Manga> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true
)