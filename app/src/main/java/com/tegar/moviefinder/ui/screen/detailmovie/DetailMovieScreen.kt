package com.tegar.moviefinder.ui.screen.detailmovie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.tegar.moviefinder.R
import com.tegar.moviefinder.data.model.DetailResponse
import com.tegar.moviefinder.data.model.GenresItem
import com.tegar.moviefinder.data.model.Movie
import com.tegar.moviefinder.ui.common.UiState
import com.tegar.moviefinder.ui.component.EmptyUI
import com.tegar.moviefinder.ui.component.ErrorUI
import com.tegar.moviefinder.ui.component.FavoriteButton
import com.tegar.moviefinder.ui.component.LoadingUI
import com.tegar.moviefinder.ui.screen.favorite.FavoriteViewModel
import com.tegar.moviefinder.utils.ImageTmdbQuality

@Composable
fun DetailMovieScreen(
    modifier: Modifier = Modifier,
    movieId: Int,
    detailViewModel: DetailViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    LaunchedEffect(movieId) {
        detailViewModel.getDetail(movieId)
    }

    val uiState by detailViewModel.data.collectAsState()

    when (uiState) {
        is UiState.Loading -> LoadingUI()
        is UiState.Success -> {
            val data = (uiState as UiState.Success<DetailResponse>).data
            DetailContent(
                data = data,
                favoriteViewModel = favoriteViewModel,
                navigateBack = navigateBack,
                modifier = modifier
            )
        }

        is UiState.Error -> ErrorUI(uiState as UiState.Error)
        is UiState.Empty -> EmptyUI()
    }
}

@Composable
private fun DetailContent(
    data: DetailResponse,
    favoriteViewModel: FavoriteViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier,
) {
    LazyColumn {
        item {
            MovieHeader(
                posterPath = data.posterPath,
                id = data.id,
                navigateBack = navigateBack,
                modifier = modifier
            )
        }
        item {
            MovieTitleAndFavoriteButton(
                id = data.id,
                title = data.title,
                genres = data.genres,
                posterPath = data.posterPath,
                releaseDate = data.releaseDate,
                favoriteViewModel = favoriteViewModel
            )
        }
        item {
            OverviewSection(overview = data.overview, modifier = modifier)
        }
        item {
            AdditionalDetailsSection(
                adult = data.adult,
                releaseDate = data.releaseDate,
                voteAverage = data.voteAverage,
                runtime = data.runtime,
                modifier = modifier
            )
            Spacer(modifier = modifier.height(30.dp))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MovieHeader(
    posterPath: String?,
    id: Int,
    navigateBack: () -> Unit,
    modifier: Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        GlideImage(
            model = posterPath?.let { ImageTmdbQuality.qualityOriginal(it) },
            contentDescription = id.toString(),
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(0.75F)
                .align(Alignment.TopStart),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.placeholder),
            failure = placeholder(R.drawable.error)
        )
        IconButton(
            onClick = navigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.Black.copy(alpha = 0.75F))
                .testTag("backButton")

        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = stringResource(R.string.back),
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 10.dp)
            )
        }
    }
}

@Composable
private fun MovieTitleAndFavoriteButton(
    id: Int,
    title: String,
    genres: List<GenresItem?>?,
    posterPath: String?,
    releaseDate: String,
    favoriteViewModel: FavoriteViewModel,
) {
    val isFavorite by favoriteViewModel.isFavorite(id).observeAsState(initial = false)

    val genreList = genres?.map { it?.name }
    val genreText = genreList?.joinToString(", ")
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1F)
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = genreText ?: stringResource(R.string.minus),
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                color = Color.Gray
            )
        }
        FavoriteButton(isFavorite = isFavorite) {
            if (isFavorite) {
                favoriteViewModel.deleteByIdMovie(id)
            } else {
                favoriteViewModel.insertMovie(
                    Movie(
                        id = id,
                        title = title,
                        posterPath = posterPath,
                        releaseDate = releaseDate
                    )
                )
            }
        }
    }
}

@Composable
private fun OverviewSection(overview: String?, modifier: Modifier) {
    overview?.let {
        Text(
            text = it,
            fontSize = 18.sp,
            fontWeight = FontWeight.W400,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun AdditionalDetailsSection(
    adult: Boolean,
    releaseDate: String,
    voteAverage: Double?,
    runtime: Int,
    modifier: Modifier,
) {
    val isAdult = if (adult) {
        stringResource(R.string.child)
    } else {
        stringResource(R.string._18)
    }
    val year = if (releaseDate.isNotEmpty()) {
        releaseDate.split("-")[0]
    } else {
        stringResource(R.string.minus)
    }
    LazyRow(modifier = modifier.padding(8.dp)) {
        item {
            DetailItem(text = isAdult, modifier = modifier)
        }
        item {
            DetailItem(text = stringResource(R.string.year, year), modifier = modifier)
        }
        item {
            DetailItem(text = stringResource(R.string.minutes, runtime), modifier = modifier)
        }
        item {
            DetailItem(
                text = stringResource(R.string.star, voteAverage.toString()),
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DetailItem(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.DarkGray)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

