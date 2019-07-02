package com.rubahapi.moviedb.api

import com.rubahapi.moviedb.BuildConfig.BASE_URL
import com.rubahapi.moviedb.BuildConfig.TMDB_API_KEY

object TheMovieDbApi {
    fun getMovieList(): String {
        return "$BASE_URL/discover/movie?api_key=$TMDB_API_KEY&language=en-US"
    }
}