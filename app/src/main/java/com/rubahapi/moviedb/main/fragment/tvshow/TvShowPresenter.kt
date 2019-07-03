package com.rubahapi.moviedb.main.fragment.tvshow

import android.support.v4.app.Fragment
import com.google.gson.Gson
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.api.TheMovieDbApi
import com.rubahapi.moviedb.model.MovieResponse
import com.rubahapi.moviedb.model.TvShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TvShowPresenter(private val view: TvShow,
                      private val apiRepository: ApiRepository,
                      private val gson: Gson
                     ): TvShowPresenterView<TvShowFragment> {
    private var mView: Fragment? = null

    fun getTvShow(){
//        view.showLoading()
//        GlobalScope.launch(Dispatchers.Main) {
//            val data = gson.fromJson(apiRepository.doRequest(TheMovieDbApi.getMovieList()).await(), MovieResponse::class.java)
//            view.showMovie(data.movies)
//            view.hideLoading()
//        }
    }

    override fun onAttach(view: TvShowFragment) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

}