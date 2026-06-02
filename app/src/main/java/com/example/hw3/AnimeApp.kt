package com.example.hw3

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hw3.ui1.AnimeViewModel
import com.example.hw3.ui1.screens.AnimeDetailsScreen
import com.example.hw3.ui1.screens.AnimeListScreen

@Composable
fun AnimeApp() {

    val vm: AnimeViewModel = viewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {

        composable(Routes.LIST) {

            AnimeListScreen(
                uiState = vm.uiState,
                onSearchChange = vm::onSearchQueryChange,
                onAnimeClick = { id ->
                    navController.navigate(
                        Routes.details(id)
                    )
                },
                onRetry = vm::retry
            )
        }

        composable(
            route = Routes.DETAILS_ROUTE,
            arguments = listOf(
                navArgument(Routes.ANIME_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getInt(Routes.ANIME_ID)
                ?: return@composable

            AnimeDetailsScreen(
                animeId = id,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}