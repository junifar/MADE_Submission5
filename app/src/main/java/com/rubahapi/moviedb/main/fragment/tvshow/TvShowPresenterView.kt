package com.rubahapi.moviedb.main.fragment.tvshow

import androidx.fragment.app.Fragment

interface TvShowPresenterView<T: Fragment> {
    fun onAttach(view: T)
    fun onDetach()
}