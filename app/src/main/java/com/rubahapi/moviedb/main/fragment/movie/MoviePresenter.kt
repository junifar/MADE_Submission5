package com.rubahapi.moviedb.main.fragment.movie

import android.support.v4.app.Fragment
import com.google.gson.Gson
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.api.TheMovieDbApi
import com.rubahapi.moviedb.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoviePresenter(private val view: MovieView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson
                     ): MoviePresenterView<MovieFragment> {
    private var mView: Fragment? = null

    fun getMovie(){
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            val data = gson.fromJson(apiRepository.doRequest(TheMovieDbApi.getMovieList()), MovieResponse::class.java)
            view.showMovie(data.movies)
            view.hideLoading()
        }
    }

    override fun onAttach(view: MovieFragment) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

}