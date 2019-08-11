package com.rubahapi.moviedb.main.fragment.tvshow

import com.rubahapi.moviedb.model.TvShow

interface FavoriteTVShowView {
    fun showLoading()
    fun hideLoading()
    fun onAttachView()
    fun onDetachView()
    fun showTvShow(data: List<TvShow>)
}