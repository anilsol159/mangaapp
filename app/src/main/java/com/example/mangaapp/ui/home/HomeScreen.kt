package com.example.mangaapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mangaapp.ui.face.FaceDetectionScreen
import com.example.mangaapp.ui.manga.MangaScreen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Manga : BottomNavItem("manga", "Manga", Icons.Filled.MenuBook)
    object FaceDetection : BottomNavItem("face_detection", "Face Detection", Icons.Filled.Face)
}

@Composable
fun HomeScreen(onMangaClick: (String) -> Unit) {
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    val bottomNavItems = listOf(
        BottomNavItem.Manga,
        BottomNavItem.FaceDetection
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                0 -> MangaScreen( onMangaClick =  { mangaId ->
                    onMangaClick(mangaId)
                })
                1 -> FaceDetectionScreen()
            }
        }
    }
}