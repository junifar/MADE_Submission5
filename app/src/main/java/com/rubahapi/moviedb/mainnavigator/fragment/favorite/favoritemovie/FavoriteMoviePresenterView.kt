package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritemovie

import androidx.fragment.app.Fragment

interface FavoriteMoviePresenterView<T: Fragment> {
    fun onAttach(view: T)
    fun onDetach()
}