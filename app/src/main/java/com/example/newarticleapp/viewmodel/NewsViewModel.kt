package com.example.newarticleapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newarticleapp.data.Article
import com.example.newarticleapp.repository.NewsRepository
import com.example.newarticleapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<Article>>>(Resource.Loading())
    val uiState: StateFlow<Resource<List<Article>>> = _uiState

    fun fetchTopHeadlines() {
        viewModelScope.launch {
            repository.getTopHeadlines()
                .collectLatest { resource ->
                    _uiState.value = resource
                }
        }
    }
}
