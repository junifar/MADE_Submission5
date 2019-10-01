package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritemovie

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

class FavoriteMoviePresenter(private val view: FavoriteMovieView,
                             private val apiRepository: ApiRepository,
                             private val gson: Gson
                     ): FavoriteMoviePresenterView<FavoriteMovieFragment> {
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
            view.showMovie(data.movies)
            view.hideLoading()
        }
    }

    override fun onAttach(view: FavoriteMovieFragment) {
        mView = view
    }

    override fun onDetach() {
        mView = null
    }

}