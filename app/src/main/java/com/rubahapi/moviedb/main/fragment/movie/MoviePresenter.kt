package com.rubahapi.moviedb.main.fragment.movie

import android.support.v4.app.Fragment

class MoviePresenter(private val view: MovieView): MoviePresenterView<MovieFragment> {
    private var mView: Fragment? = null

    override fun onAttach(view: MovieFragment) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

}