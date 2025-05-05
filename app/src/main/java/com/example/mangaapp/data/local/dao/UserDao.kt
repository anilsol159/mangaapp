package com.example.mangaapp.data.local.dao

import androidx.room.*
import com.example.mangaapp.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<UserEntity?>

    @Query("UPDATE users SET isLoggedIn = 1 WHERE email = :email")
    suspend fun loginUser(email: String)

    @Query("UPDATE users SET isLoggedIn = 0 WHERE email = :email")
    suspend fun logoutUser(email: String)
}