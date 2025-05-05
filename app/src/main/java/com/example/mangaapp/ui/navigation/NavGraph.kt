package com.example.mangaapp.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mangaapp.ui.home.HomeScreen
import com.example.mangaapp.ui.mangadetail.MangaDetailScreen
import com.example.mangaapp.ui.signin.SignInScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen { mangaId ->
                navController.navigate(Screen.MangaDetail.createRoute(mangaId))
            }
        }

        composable(
            route = Screen.MangaDetail.route,
            arguments = listOf(navArgument("mangaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
            MangaDetailScreen(
                mangaId = mangaId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object SignIn : Screen("sign_in")
    object Home : Screen("home")
    object MangaDetail : Screen("manga_detail/{mangaId}") {
        fun createRoute(mangaId: String) = "manga_detail/$mangaId"
    }
}