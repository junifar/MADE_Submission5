package com.rubahapi.moviedb.main.fragment.movie

import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment
import android.util.Log
import com.google.gson.Gson
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.api.TheMovieDbApi
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.MovieResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoviePresenter(private val view: MovieView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson
                     ): MoviePresenterView<MovieFragment> {
    private var mView: Fragment? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(TAG, "$exception handled !")
        val listData = mutableListOf<Movie>()
        listData.add(Movie(0,"No Connection Detected", "",""))
        view.showMovie(listData)
        view.hideLoading()
    }

    fun getMovie(){
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main + handler) {
            val data = gson.fromJson(apiRepository.doRequest(TheMovieDbApi.getMovieList()).await(), MovieResponse::class.java)
            if(data.movies.isNullOrEmpty()) view.showBlankMovie() else view.showMovie(data.movies)
            view.hideLoading()
        }
    }

    fun getMovie(movieKey:String){
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main + handler) {
            val data = gson.fromJson(apiRepository.doRequest(TheMovieDbApi.getFilterMovieList(movieKey)).await(), MovieResponse::class.java)
            if(data.movies.isNullOrEmpty()) view.showBlankMovie() else view.showMovie(data.movies)
            view.hideLoading()
        }
    }

     fun onAttach(view: MovieFragment) {
        mView = view
    }

     fun onDetach() {
        mView = null
    }

}