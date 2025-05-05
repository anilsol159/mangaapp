package com.example.mangaapp.data.repository


import com.example.mangaapp.data.local.dao.UserDao
import com.example.mangaapp.data.local.entities.UserEntity
import com.example.mangaapp.domain.model.User
import com.example.mangaapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun createOrLoginUser(email: String, password: String): Result<User> {
        return try {
            val existingUser = userDao.getUserByEmail(email)

            if (existingUser != null) {
                // Verify password
                if (existingUser.password == password) {
                    userDao.loginUser(email)
                    Result.success(existingUser.toDomainModel())
                } else {
                    Result.failure(Exception("Invalid password"))
                }
            } else {
                // Create new user
                val newUser = UserEntity(email = email, password = password, isLoggedIn = true)
                userDao.insertUser(newUser)
                Result.success(newUser.toDomainModel())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Flow<User?> {
        return userDao.getLoggedInUser()
            .map { it?.toDomainModel() }
    }

    override suspend fun logout(email: String) {
        userDao.logoutUser(email)
    }
}

private fun UserEntity.toDomainModel(): User {
    return User(
        id = id,
        email = email,
        password = password,
        isLoggedIn = isLoggedIn
    )
}
