package com.example.mangaapp.ui.mangadetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.domain.model.Manga
import com.example.mangaapp.domain.usecase.GetMangaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaDetailViewModel @Inject constructor(
    private val getMangaUseCase: GetMangaUseCase
) : ViewModel() {

    private val _manga = MutableStateFlow<Manga?>(null)
    val manga: StateFlow<Manga?> = _manga.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadMangaDetail(mangaId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _manga.value = getMangaUseCase.getMangaById(mangaId)
            _isLoading.value = false
        }
    }
}