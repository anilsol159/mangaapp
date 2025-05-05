package com.example.mangaapp.domain.repository

import com.example.mangaapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createOrLoginUser(email: String, password: String): Result<User>
    suspend fun getCurrentUser(): Flow<User?>
    suspend fun logout(email: String)
}