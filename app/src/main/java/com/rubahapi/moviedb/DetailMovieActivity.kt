package com.rubahapi.moviedb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.TvShow

class DetailMovieActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_DETAIL_MOVIE = "DetailMovies"
        const val EXTRA_DETAIL_TV_SHOW = "DetailTVShow"
        const val EXTRA_DETAIL_ACTIVITY_TYPE = "DetailActovotyType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        when (intent.getStringExtra(EXTRA_DETAIL_ACTIVITY_TYPE)){
            EXTRA_DETAIL_MOVIE -> this.detailMovieInit()
            else -> this.detailTvShowInit()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
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
