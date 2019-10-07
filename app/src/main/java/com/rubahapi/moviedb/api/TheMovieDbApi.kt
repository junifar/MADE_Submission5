package com.rubahapi.moviedb.api

import com.rubahapi.moviedb.BuildConfig.BASE_URL
import com.rubahapi.moviedb.BuildConfig.TMDB_API_KEY

object TheMovieDbApi {
    fun getMovieList(): String {
        return "$BASE_URL/discover/movie?api_key=$TMDB_API_KEY&language=en-US"
    }

    fun getTvShowList():String{
        return "$BASE_URL/discover/tv?api_key=$TMDB_API_KEY&language=en-US"
    }

    fun getTodayRelease(releaseDate:String):String{
        return "$BASE_URL/discover/movie?api_key=$TMDB_API_KEY&primary_release_date.gte=$releaseDate&primary_release_date.lte=$releaseDate"
    }

    fun getFilterMovieList(movieKey:String):String{
        return "$BASE_URL/search/movie?api_key=$TMDB_API_KEY&language=en-US&query=$movieKey"
    }

    fun getFilterTvList(tvKey:String):String{
        return "$BASE_URL/search/tv?api_key=$TMDB_API_KEY&language=en-US&query=$tvKey"
    }
}