package com.example.mangaapp.ui.signin


sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Success(val message: String) : SignInState()
    data class Error(val message: String) : SignInState()
}