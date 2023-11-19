package com.tegar.moviefinder.ui.screen.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tegar.moviefinder.data.MovieRepository
import com.tegar.moviefinder.data.model.Movie
import com.tegar.moviefinder.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _data = MutableStateFlow<UiState<List<Movie>>>(UiState.Loading)
    val data: StateFlow<UiState<List<Movie>>> = _data
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText

    init {
        initData()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun initData() {
        viewModelScope.launch(Dispatchers.IO.limitedParallelism(10)) {
            repository.getRecomendationMovie().collect { result ->
                _data.value = result
            }
        }
    }

    fun setSearchText(text: String) {
        _searchText.value = text
    }

    fun searchMovie(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchMovie(title).collect { result ->
                _data.value = result
            }
        }
    }
}
