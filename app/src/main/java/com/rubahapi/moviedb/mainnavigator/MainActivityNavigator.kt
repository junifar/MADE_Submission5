package com.rubahapi.moviedb.mainnavigator

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.mainnavigator.fragment.MovieFragment

class MainActivityNavigator : AppCompatActivity() {

    private  var savedInstanceState:Bundle = Bundle()
    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_movie -> {
//                textMessage.setText(R.string.title_home)
                loadMovieFragment(savedInstanceState)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
//                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun loadMovieFragment(savedInstanceState: Bundle?){
        if (savedInstanceState == null){
            val fragment = MovieFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_container, fragment, MovieFragment::class.java.simpleName)
                .commit()
        }
    }

    fun loadFavoriteFragment(savedInstanceState: Bundle?){
        if (savedInstanceState == null){

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigator)
        val navView: BottomNavigationView = findViewById(R.id.navigation_view)

        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState
        }

        loadMovieFragment(savedInstanceState)
//        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
