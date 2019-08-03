package com.georgcantor.kotlincorutines.model

import com.georgcantor.kotlincorutines.BuildConfig

object Constants {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val PHOTO_URL = " http://image.tmdb.org/t/p/w185"
    var apiKey = BuildConfig.TMDB_API_KEY
}