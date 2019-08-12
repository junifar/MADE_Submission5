package com.rubahapi.moviedb

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.rubahapi.moviedb.db.database
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.TvShow

class DetailMovieActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_DETAIL_MOVIE = "DetailMovies"
        const val EXTRA_DETAIL_TV_SHOW = "DetailTVShow"
        const val EXTRA_DETAIL_ACTIVITY_TYPE = "DetailActovotyType"
    }

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_movie_menu, menu)
        menuItem = menu
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        when (intent.getStringExtra(EXTRA_DETAIL_ACTIVITY_TYPE)){
            EXTRA_DETAIL_MOVIE -> this.detailMovieInit()
            else -> this.detailTvShowInit()
        }

    }

    private fun removeFromFavorite(){
        Toast.makeText(this, "Removed from favorite",Toast.LENGTH_SHORT).show()
    }

    private fun addToFavorite(){
        val movie = Movie("Test", "uji", "aa")
        val movieHelper = database.getInstance(this.applicationContext)
        movieHelper.open()
        val result = movieHelper.insertMovie(movie)
        movieHelper.close()
        println(result)
        Toast.makeText(this, "Added to favorite",Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun detailTvShowInit(){
        val tvShow = intent.getParcelableExtra<TvShow>(EXTRA_DETAIL_TV_SHOW)

        val imageLogo = findViewById<ImageView>(R.id.image_logo)
        val textMovieName = findViewById<TextView>(R.id.textMovieName)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)

        Glide.with(this).load("https://image.tmdb.org/t/p/w370_and_h556_bestv2${tvShow.poster_path}")
            .into(imageLogo)
        textMovieName.text = tvShow.name
        textViewDescription.text = tvShow.overview
    }

    private fun detailMovieInit(){
        val movie = intent.getParcelableExtra<Movie>(EXTRA_DETAIL_MOVIE)

        val image_logo = findViewById<ImageView>(R.id.image_logo)
        val textMovieName = findViewById<TextView>(R.id.textMovieName)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)

        Glide.with(this).load("https://image.tmdb.org/t/p/w370_and_h556_bestv2${movie.poster_path}")
            .into(image_logo)
        textMovieName.text = movie.title
        textViewDescription.text = movie.overview
    }
}
