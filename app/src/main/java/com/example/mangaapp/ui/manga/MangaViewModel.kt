package com.example.mangaapp.ui.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangaapp.domain.usecase.GetMangaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val getMangaUseCase: GetMangaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MangaState())
    val state: StateFlow<MangaState> = _state.asStateFlow()

    init {
        loadManga()
    }

    fun loadManga(page: Int = 1) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            getMangaUseCase.fetchManga(page).collect { mangaList ->
                _state.value = _state.value.copy(
                    mangaList = if (page == 1) mangaList else _state.value.mangaList + mangaList,
                    isLoading = false,
                    error = null,
                    currentPage = page,
                    hasMorePages = mangaList.isNotEmpty()
                )
            }
        }
    }

    fun loadNextPage() {
        if (_state.value.hasMorePages && !_state.value.isLoading) {
            loadManga(_state.value.currentPage + 1)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)

            getMangaUseCase.fetchManga(1).collect { mangaList ->
                _state.value = _state.value.copy(
                    mangaList = mangaList,
                    isRefreshing = false,
                    error = null,
                    currentPage = 1,
                    hasMorePages = mangaList.isNotEmpty()
                )
            }
        }
    }
}