package com.example.hw3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hw3.ui1.AnimeDetailsUiState
import com.example.hw3.ui1.AnimeDetailsViewModel
import com.example.hw3.ui1.AnimeViewModel
import com.example.hw3.ui1.FavouritesViewModel
import com.example.hw3.ui1.screens.AnimeDetailsScreen
import com.example.hw3.ui1.screens.AnimeListScreen
import com.example.hw3.ui1.screens.FavouritesScreen

@Composable
fun AnimeApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {

        composable(Routes.LIST) {

            val vm: AnimeViewModel = hiltViewModel()

            AnimeListScreen(
                uiState = vm.uiState,
                searchQuery = vm.searchQuery,
                onSearchChange = vm::onSearchQueryChange,
                onAnimeClick = { id ->
                    navController.navigate(Routes.details(id))
                },
                onRetry = vm::retry,
                onFavouritesClick = {
                    navController.navigate(Routes.FAVOURITES)
                }
            )
        }

        composable(Routes.FAVOURITES) {

            val vm: FavouritesViewModel = hiltViewModel()

            FavouritesScreen(
                favouritesFlow = vm.favourites,
                onAnimeClick = { id ->
                    navController.navigate(Routes.details(id))
                },
                onRemove = vm::remove,
                onBack = {
                    navController.popBackStack()
                }
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

            val id = backStackEntry.arguments?.getInt(Routes.ANIME_ID)
                ?: return@composable

            val vm: AnimeDetailsViewModel = hiltViewModel()
            val isFavourite by vm.isFavourite.collectAsState()

            LaunchedEffect(id) {
                vm.loadAnime(id)
            }

            AnimeDetailsScreen(
                state = vm.uiState,
                isFavourite = isFavourite,
                onBack = {
                    navController.popBackStack()
                },
                onRetry = {
                    vm.loadAnime(id)
                },
                onToggleFavourite = {
                    val s = vm.uiState
                    if (s is AnimeDetailsUiState.Success) {
                        vm.toggleFavourite(s.anime)
                    }
                }
            )
        }
    }
}