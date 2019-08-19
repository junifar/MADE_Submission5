package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritetvshow

import android.support.v4.app.Fragment

interface FavoriteTvShowPresenterView<T: Fragment> {
    fun onAttach(view: T)
    fun onDetach()
}