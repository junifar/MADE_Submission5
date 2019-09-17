package com.rubahapi.moviedb.mainnavigator

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.db.DatabaseHelper
import com.rubahapi.moviedb.mainnavigator.fragment.MovieFragment
import com.rubahapi.moviedb.mainnavigator.fragment.favorite.FavoriteFragment
import com.rubahapi.moviedb.model.Movie

class MainActivityNavigator : AppCompatActivity(), SearchView.OnQueryTextListener{

    private lateinit var menuItem: MenuItem
    private val fm = supportFragmentManager

    private fun newMovie(){
        val helper = DatabaseHelper(this, null, null, 1)
        val movie = Movie(1, "Title111", "overview", "path")
        helper.addMovie(movie)
    }

    override fun onQueryTextSubmit(search: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(search: String?): Boolean {
        val fragment = fm.findFragmentById(R.id.home_container) as MovieFragment
        if (search != null) {
            fragment.filterList(search)
        }
        return true
    }

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
        newMovie()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView

        menuItem = menu.findItem(R.id.action_search) as MenuItem

        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(this.componentName)
        )
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onCreateOptionsMenu(menu: Menu) {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        val searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
//
//        menuItem = menu.findItem(R.id.action_search) as MenuItem
//
//        searchView.setSearchableInfo(
//            searchManager
//                .getSearchableInfo(this.componentName)
//        )
//        searchView.maxWidth = Integer.MAX_VALUE
//
//        searchView.setOnQueryTextListener(this)
//        super.onCreateOptionsMenu(menu)
//    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_change_settings){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
