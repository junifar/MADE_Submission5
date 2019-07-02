package com.rubahapi.moviedb.main.fragment.movie

import com.rubahapi.moviedb.model.Movie

interface MovieView {
    fun showLoading()
    fun hideLoading()
    fun showBlankData()
    fun onAttachView()
    fun onDetachView()
    fun showMovie(data: List<Movie>)
}