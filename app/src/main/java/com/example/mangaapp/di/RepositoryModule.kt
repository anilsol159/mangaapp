package com.example.mangaapp.di

import com.example.mangaapp.data.repository.MangaRepositoryImpl
import com.example.mangaapp.data.repository.UserRepositoryImpl
import com.example.mangaapp.domain.repository.MangaRepository
import com.example.mangaapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindMangaRepository(
        mangaRepositoryImpl: MangaRepositoryImpl
    ): MangaRepository
}