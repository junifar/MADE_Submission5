package com.rubahapi.moviedb.main.fragment.tvshow

import android.support.v4.app.Fragment

interface FavoriteTvShowPresenterView<T: Fragment> {
    fun onAttach(view: T)
    fun onDetach()
}