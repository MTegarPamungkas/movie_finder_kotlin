package com.tegar.moviefinder.ui.screen.favorite

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tegar.moviefinder.data.model.Movie
import com.tegar.moviefinder.ui.common.UiState
import com.tegar.moviefinder.ui.component.EmptyUI
import com.tegar.moviefinder.ui.component.ErrorUI
import com.tegar.moviefinder.ui.component.LoadingUI
import com.tegar.moviefinder.ui.component.MovieItem

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit,
) {
    val uiState by favoriteViewModel.getAllMovie.collectAsState(initial = UiState.Loading)
    when (uiState) {
        is UiState.Loading -> LoadingUI()
        is UiState.Success -> {
            val configuration = LocalConfiguration.current
            val columns =
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
            val data = (uiState as UiState.Success<List<Movie>>).data
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(data, key = { item -> item.id }) { item ->
                    MovieItem(
                        id = item.id,
                        posterPath = item.posterPath,
                        releaseDate = item.releaseDate,
                        title = item.title,
                        modifier = modifier.clickable {
                            navigateToDetail(item.id)
                        }
                    )
                }
            }
        }

        is UiState.Error -> ErrorUI(uiState as UiState.Error)

        is UiState.Empty -> EmptyUI()
    }
}