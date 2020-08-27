package com.georgcantor.kotlincorutines.util

object Constants {
    // an error occurs when there is no Internet and no data in the cache
    const val ERROR_504 = "HTTP 504 Unsatisfiable Request (only-if-cached)"

    const val ARG_ARTICLE = "article"
    const val ARG_QUERY = "query"

    const val BUSINESS_PAGE = 0
    const val SCIENCE_PAGE = 1
    const val SPORT_PAGE = 2

    const val ANIM_PLAYBACK_SPEED: Double = 0.8
}