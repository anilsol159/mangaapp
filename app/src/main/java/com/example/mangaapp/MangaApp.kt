package com.example.mangaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MangaApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}