package com.example.newarticleapp.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>?
)

@JsonClass(generateAdapter = true)
data class Article(
    val title: String?,
    val description: String?,
    @Json(name = "publishedAt") val publishedAt: String?,
    @Json(name = "urlToImage") val urlToImage: String?,
    val url: String?
)