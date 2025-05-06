package com.example.mangaapp.di

import android.content.Context
import com.example.mangaapp.ui.face.FaceDetectorHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FaceDetectionModule {

    @Provides
    @Singleton
    fun provideFaceDetectorHelper(@ApplicationContext context: Context): FaceDetectorHelper {
        return FaceDetectorHelper(context)
    }
}