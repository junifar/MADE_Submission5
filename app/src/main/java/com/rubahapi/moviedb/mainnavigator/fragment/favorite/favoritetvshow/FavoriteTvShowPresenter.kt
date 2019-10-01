package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritetvshow

import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment
import android.util.Log
import com.google.gson.Gson
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.api.TheMovieDbApi
import com.rubahapi.moviedb.model.TvShow
import com.rubahapi.moviedb.model.TvShowResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteTvShowPresenter(private val view: FavoriteTVShowView,
                              private val apiRepository: ApiRepository,
                              private val gson: Gson
                     ): FavoriteTvShowPresenterView<FavoriteTvShowFragment> {
    private var mView: Fragment? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG, "$exception handled !")
        val listData = mutableListOf<TvShow>()
        listData.add(TvShow(0,"No Connection Detected", "", ""))
        view.showTvShow(listData)
        view.hideLoading()
    }

    override fun onAttach(view: FavoriteTvShowFragment) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

}