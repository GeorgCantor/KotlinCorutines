package com.georgcantor.kotlincorutines.model.data

data class Movie(
    val id: Int,
    val vote_average: Double,
    val title: String,
    val overview: String,
    val adult: Boolean
)