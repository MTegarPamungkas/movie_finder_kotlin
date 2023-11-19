package com.tegar.moviefinder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tegar.moviefinder.ui.navigation.BottomBar
import com.tegar.moviefinder.ui.navigation.Screen
import com.tegar.moviefinder.ui.screen.about.AboutScreen
import com.tegar.moviefinder.ui.screen.detailmovie.DetailMovieScreen
import com.tegar.moviefinder.ui.screen.favorite.FavoriteScreen
import com.tegar.moviefinder.ui.screen.movie.MovieScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieFinder(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                if (currentRoute != Screen.DetailMovie.route) {
                    BottomBar(navController)
                }
            }, modifier = modifier
        ) { paddingValues ->
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Movie.route,
                    modifier = modifier.padding(paddingValues)
                ) {
                    composable(Screen.Movie.route) {
                        MovieScreen(navigateToDetail = { movieId ->
                            navController.navigate(Screen.DetailMovie.createRoute(movieId))
                        })
                    }
                    composable(
                        route = Screen.DetailMovie.route,
                        arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val movieId = arguments.getInt("movieId")
                        DetailMovieScreen(
                            movieId = movieId,
                            navigateBack = {
                                navController.navigateUp()
                            },
                        )
                    }
                    composable(Screen.Favourite.route) {
                        FavoriteScreen(
                            navigateToDetail = { movieId ->
                                navController.navigate(Screen.DetailMovie.createRoute(movieId))
                            },
                        )
                    }
                    composable(Screen.About.route) {
                        AboutScreen()
                    }

                }
            }
        }

    }
}
