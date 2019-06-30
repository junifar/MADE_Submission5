package com.rubahapi.moviedb.main.fragment.movie

interface MovieView {
    fun showLoading()
    fun hideLoading()
    fun showBlankData()
    fun onAttachView()
    fun onDetachView()
}