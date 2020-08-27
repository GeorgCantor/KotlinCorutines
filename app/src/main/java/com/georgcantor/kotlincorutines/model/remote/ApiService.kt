package com.georgcantor.kotlincorutines.model.remote

import com.georgcantor.kotlincorutines.BuildConfig.API_KEY
import com.georgcantor.kotlincorutines.model.response.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<News>
}