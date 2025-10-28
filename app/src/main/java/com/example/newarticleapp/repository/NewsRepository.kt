package com.example.newarticleapp.repository

import com.example.newarticleapp.api.NewsApiService
import com.example.newarticleapp.data.Article
import com.example.newarticleapp.utils.Resource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Collections.emptyList

class NewsRepository @Inject constructor(
    private val apiService: NewsApiService
) {
    fun getTopHeadlines(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getTopHeadlines("techcrunch", "4d246d73f7c74953bdc6c015fcecad41")
            val articles = response.articles ?: emptyList()
            emit(Resource.Success(articles))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
        }
    }}