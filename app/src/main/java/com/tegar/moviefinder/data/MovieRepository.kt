package com.tegar.moviefinder.data

import com.tegar.moviefinder.data.local.MovieDao
import com.tegar.moviefinder.data.model.DetailResponse
import com.tegar.moviefinder.data.model.Movie
import com.tegar.moviefinder.data.remote.retrofit.ApiService
import com.tegar.moviefinder.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao,
) {

    fun getRecomendationMovie(): Flow<UiState<List<Movie>>> = flow {
        try {
            emit(UiState.Loading)
            val response = apiService.getRecomendationMovie(page = 1)
            val movies = response.results?.filterNotNull()
            if (!movies.isNullOrEmpty()) {
                emit(UiState.Success(movies))
            } else {
                emit(UiState.Empty)
            }
        } catch (e: IOException) {
            emit(UiState.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            emit(UiState.Error("An error occurred: ${e.message}"))
        }
    }

    fun searchMovie(title: String): Flow<UiState<List<Movie>>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.searchMovie(title)
            val movies = response.results?.filterNotNull()
            if (!movies.isNullOrEmpty()) {
                emit(UiState.Success(movies))
            } else {
                emit(UiState.Empty)
            }
        } catch (e: IOException) {
            emit(UiState.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            emit(UiState.Error("An error occurred: ${e.message}"))
        }
    }

    fun detailMovie(id: Int): Flow<UiState<DetailResponse>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.detailMovie(id)
            emit(UiState.Success(response))
        } catch (e: IOException) {
            emit(UiState.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            emit(UiState.Error("An error occurred: ${e.message}"))
        }
    }

    fun getAllMovieDB(): Flow<UiState<List<Movie>>> = flow {
        emit(UiState.Loading)
        try {
            movieDao.getAllMovie().collect { movie ->
                if (movie.isEmpty()) {
                    emit(UiState.Empty)
                } else {
                    emit(UiState.Success(movie))
                }
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }

    fun isFavoriteDB(id: Int): Flow<Boolean> = movieDao.isFavorite(id)

    suspend fun insertMovieDB(movie: Movie): UiState<Unit> {
        return try {
            movieDao.insertMovie(movie)
            UiState.Success(Unit)
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun deleteMovie(id: Int): Boolean {
        return movieDao.deleteMovie(id)
    }
}