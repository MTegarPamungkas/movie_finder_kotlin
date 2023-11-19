package com.tegar.moviefinder.ui.navigation

sealed class Screen(val route: String) {
    data object Movie : Screen("movie")
    data object Favourite : Screen("favorite")
    data object About : Screen("about")
    data object DetailMovie : Screen("movie/{movieId}") {
        fun createRoute(movieId: Int) = "movie/$movieId"
    }
}
