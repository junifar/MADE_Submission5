package com.rubahapi.moviedb.mainnavigator

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.mainnavigator.fragment.MovieFragment
import com.rubahapi.moviedb.mainnavigator.fragment.favorite.FavoriteFragment

class MainActivityNavigator : AppCompatActivity(){

    private  var savedInstanceState:Bundle = Bundle()
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_movie -> {
                loadMovieFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
                loadFavoriteFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadMovieFragment() {
            val fragment = MovieFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_container, fragment, MovieFragment::class.java.simpleName)
                .commit()
    }

    private fun loadFavoriteFragment() {
            val fragment = FavoriteFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_container, fragment, FavoriteFragment::class.java.simpleName)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigator)
        val navView: BottomNavigationView = findViewById(R.id.navigation_view)

        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState
        }

        loadMovieFragment()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

}
