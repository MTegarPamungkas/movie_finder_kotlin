package com.tegar.moviefinder.ui.screen.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tegar.moviefinder.data.MovieRepository
import com.tegar.moviefinder.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    val getAllMovie = repository.getAllMovieDB()

    fun isFavorite(id: Int): LiveData<Boolean> = repository.isFavoriteDB(id).asLiveData()

    fun insertMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovieDB(movie)
    }

    fun deleteByIdMovie(id: Int) = viewModelScope.launch {
        repository.deleteMovie(id)
    }

}


