package com.example.newarticleapp.utils

import com.example.newarticleapp.data.Article

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

 sealed class NewsUiState   {
    data class Success(val data: List<Article>?) : NewsUiState()
    data class Error(val message: String?) : NewsUiState()
    object Loading : NewsUiState()
}