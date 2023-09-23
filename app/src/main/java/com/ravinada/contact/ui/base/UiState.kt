package com.ravinada.contact.ui.base

sealed interface UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>
    data class Error<T>(val message: String?, val data: T?) : UiState<T>
    object Loading : UiState<Nothing>
}