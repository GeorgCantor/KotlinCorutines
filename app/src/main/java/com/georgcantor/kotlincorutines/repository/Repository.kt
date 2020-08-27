package com.georgcantor.kotlincorutines.repository

import com.georgcantor.kotlincorutines.model.remote.ApiService

class Repository(private val apiService: ApiService) {

    suspend fun getNews(query: String, page: Int) = apiService.getNews(query, page)
}