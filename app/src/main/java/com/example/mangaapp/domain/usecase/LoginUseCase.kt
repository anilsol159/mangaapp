package com.example.mangaapp.domain.usecase

import com.example.mangaapp.domain.model.User
import com.example.mangaapp.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Add email validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("Invalid email format"))
        }

        // Add password validation
        if (password.isEmpty()) {
            return Result.failure(Exception("Password cannot be empty"))
        }

        return userRepository.createOrLoginUser(email, password)
    }
}