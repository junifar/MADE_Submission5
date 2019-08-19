package com.rubahapi.moviedb.mainnavigator

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.mainnavigator.fragment.favorite.FavoriteFragment
import com.rubahapi.moviedb.mainnavigator.fragment.MovieFragment

class MainActivityNavigator : AppCompatActivity(),SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private lateinit var menuItem: MenuItem

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        menuItem = menu.findItem(R.id.action_search) as MenuItem

        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }
}
