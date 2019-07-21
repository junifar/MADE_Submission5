package com.rubahapi.moviedb.main.fragment.movie

import android.content.ContentValues.TAG
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.api.TheMovieDbApi
import com.rubahapi.moviedb.main.MainActivity
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.MovieResponse
import com.rubahapi.moviedb.model.TvShow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviePresenter(private val view: MovieView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson
                     ): MoviePresenterView<MovieFragment> {
    private var mView: Fragment? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG, "$exception handled !")
        val listData = mutableListOf<Movie>()
        listData.add(Movie("No Connection Detected", "",""))
        view.showMovie(listData)
        view.hideLoading()
    }

    fun getMovie(){
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main + handler) {
            val data = gson.fromJson(apiRepository.doRequest(TheMovieDbApi.getMovieList()).await(), MovieResponse::class.java)
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