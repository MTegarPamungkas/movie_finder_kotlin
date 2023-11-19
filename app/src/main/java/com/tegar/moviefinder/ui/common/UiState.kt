package com.tegar.moviefinder.ui.common

sealed class UiState<out T : Any?> {

    data object Loading : UiState<Nothing>()

    data class Success<out T : Any?>(val data: T) : UiState<T>()

    data class Error(val errorMessage: String) : UiState<Nothing>()

    data object Empty : UiState<Nothing>()
}