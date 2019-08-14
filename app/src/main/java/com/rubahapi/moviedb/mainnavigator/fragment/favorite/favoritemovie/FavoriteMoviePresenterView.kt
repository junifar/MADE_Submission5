package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritemovie

import android.support.v4.app.Fragment

interface FavoriteMoviePresenterView<T: Fragment> {
    fun onAttach(view: T)
    fun onDetach()
}