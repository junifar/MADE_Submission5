package com.rubahapi.moviedb.main.fragment.movie

import android.support.v4.app.Fragment

interface MoviePresenterView<T: Fragment> {
    fun onAttach(view: T)
    fun onDetach()
}