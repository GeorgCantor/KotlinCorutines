package com.georgcantor.kotlincorutines.model.remote

import com.georgcantor.kotlincorutines.model.data.Movie
import com.georgcantor.kotlincorutines.model.data.MoviesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovies(): Deferred<Response<MoviesResponse>>

    @GET("movie/{id}")
    fun getMovieById(@Path("id") id: Int): Deferred<Response<Movie>>
}