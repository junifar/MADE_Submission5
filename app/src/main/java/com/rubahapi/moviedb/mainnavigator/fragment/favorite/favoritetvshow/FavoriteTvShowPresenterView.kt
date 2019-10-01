package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritetvshow

import androidx.fragment.app.Fragment

interface FavoriteTvShowPresenterView<T: Fragment> {
    fun onAttach(view: T)
    fun onDetach()
}