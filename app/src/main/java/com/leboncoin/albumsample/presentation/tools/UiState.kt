package com.leboncoin.albumsample.presentation.tools

sealed interface UiState<out T> {

    data class Success<T>(val data: T) : UiState<T> , UiSuccessState<T>

    data class NetworkRestored<T>(val data: T) : UiState<T>, UiSuccessState<T>

    object ErrorNetwork : UiState<Nothing>, UiErrorState<Nothing>

    data class Error(val t: Throwable = Throwable("Unknown Error"), val payLoad: String? = null) :
        UiState<Nothing>, UiErrorState<Nothing>

    object Loading : UiState<Nothing> , UiErrorState<Nothing>

    object InitialState : UiState<Nothing>

}

sealed interface UiErrorState<out T>

sealed interface UiSuccessState<out T>
