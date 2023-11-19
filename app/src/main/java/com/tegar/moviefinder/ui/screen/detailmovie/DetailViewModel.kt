package com.tegar.moviefinder.ui.screen.detailmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tegar.moviefinder.data.MovieRepository
import com.tegar.moviefinder.data.model.DetailResponse
import com.tegar.moviefinder.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _data = MutableStateFlow<UiState<DetailResponse>>(UiState.Loading)
    val data: StateFlow<UiState<DetailResponse>> = _data

    fun getDetail(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.detailMovie(id).collect { result ->
                _data.value = result
            }
        }
    }

}
