package com.rubahapi.moviedb.main.fragment.tvshow

import android.content.ContentValues.TAG
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.api.TheMovieDbApi
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.TvShow
import com.rubahapi.moviedb.model.TvShowResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TvShowPresenter(private val view: TVShowView,
                      private val apiRepository: ApiRepository,
                      private val gson: Gson
                     ): TvShowPresenterView<TvShowFragment> {
    private var mView: Fragment? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG, "$exception handled !")
        val listData = mutableListOf<TvShow>()
        listData.add(TvShow("No Connection Detected", "", ""))
        view.showTvShow(listData)
        view.hideLoading()
    }

    fun getTvShow(){
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main + handler) {
            val data = gson.fromJson(apiRepository.doRequest(TheMovieDbApi.getTvShowList()).await(), TvShowResponse::class.java)
            view.showTvShow(data.tvShows)
            view.hideLoading()
        }
    }

    override fun onAttach(view: TvShowFragment) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

}