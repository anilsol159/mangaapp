package com.example.mangaapp.domain.usecase

import com.example.mangaapp.domain.model.User
import com.example.mangaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<User?> {
        return userRepository.getCurrentUser()
    }
}