package com.example.mangaapp.domain.model


data class User(
    val id: Int,
    val email: String,
    val password: String,
    val isLoggedIn: Boolean
)