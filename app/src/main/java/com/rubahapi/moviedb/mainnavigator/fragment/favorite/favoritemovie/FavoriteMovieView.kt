package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritemovie

import com.rubahapi.moviedb.model.Movie

interface FavoriteMovieView {
    fun showLoading()
    fun hideLoading()
    fun onAttachView()
    fun onDetachView()
    fun showMovie(data: List<Movie>)
}