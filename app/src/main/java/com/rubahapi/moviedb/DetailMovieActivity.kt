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
import com.rubahapi.moviedb.db.MovieHelper
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.TvShow

class DetailMovieActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_DETAIL_MOVIE = "DetailMovies"
        const val EXTRA_DETAIL_TV_SHOW = "DetailTVShow"
        const val EXTRA_DETAIL_ACTIVITY_TYPE = "DetailActovotyType"
    }

    private lateinit var movie: Movie
    private lateinit var tvShow: TvShow
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
//    lateinit var database:MovieHelper
    lateinit var movieHelper: MovieHelper


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

        val database = MovieHelper(this.applicationContext)
        movieHelper = database.getInstance(this.applicationContext)

        when (intent.getStringExtra(EXTRA_DETAIL_ACTIVITY_TYPE)){
            EXTRA_DETAIL_MOVIE -> this.detailMovieInit()
            else -> this.detailTvShowInit()
        }

    }

    private fun removeFromFavorite(){
        when (intent.getStringExtra(EXTRA_DETAIL_ACTIVITY_TYPE)){
            EXTRA_DETAIL_MOVIE -> {
                movieHelper.open()
                movieHelper.deleteMovie(movie.id)
                movieHelper.close()
            }
            else ->{
                movieHelper.open()
                movieHelper.deleteTvShow(tvShow.id)
                movieHelper.close()
            }
        }
        Toast.makeText(this, "Removed from favorite",Toast.LENGTH_SHORT).show()
    }

    private fun getTvShowDBByID(id: Int):List<TvShow>{

        movieHelper.open()
        val result = movieHelper.getTvShowByID(id)
        movieHelper.close()
        println(result)
        return result
    }

    private fun getMovieDBByID(id: Int):List<Movie>{
        movieHelper.open()
        val result = movieHelper.getMovieByID(id)
        movieHelper.close()
        println(result)
        return result
    }

    private fun favoriteState(){
        when (intent.getStringExtra(EXTRA_DETAIL_ACTIVITY_TYPE)){
            EXTRA_DETAIL_MOVIE -> {
                val movieData = getMovieDBByID(movie.id)
                if(movieData.isNotEmpty()) isFavorite = true
            }
            else ->{
                val tvShowData = getTvShowDBByID(tvShow.id)
                if(tvShowData.isNotEmpty()) isFavorite = true
            }
        }
    }

    private fun addToFavorite(){
        when (intent.getStringExtra(EXTRA_DETAIL_ACTIVITY_TYPE)){
            EXTRA_DETAIL_MOVIE -> {
                val movie = Movie(movie.id, movie.title, movie.overview, movie.poster_path)
                movieHelper.open()
                val result = movieHelper.insertMovie(movie)
                movieHelper.close()
                println(result)
            }
            else ->{
                val tvShow = TvShow(tvShow.id, tvShow.name, tvShow.overview, tvShow.poster_path)

                movieHelper.open()
                val result = movieHelper.insertTvShow(tvShow)
                movieHelper.close()
                println(result)
            }
        }
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
        tvShow = intent.getParcelableExtra(EXTRA_DETAIL_TV_SHOW)

        favoriteState()

        val imageLogo = findViewById<ImageView>(R.id.image_logo)
        val textMovieName = findViewById<TextView>(R.id.textMovieName)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)

        Glide.with(this).load("https://image.tmdb.org/t/p/w370_and_h556_bestv2${tvShow.poster_path}")
            .into(imageLogo)
        textMovieName.text = tvShow.name
        textViewDescription.text = tvShow.overview
    }

    private fun detailMovieInit(){
        movie = intent.getParcelableExtra(EXTRA_DETAIL_MOVIE)

        favoriteState()

        val imageLogo = findViewById<ImageView>(R.id.image_logo)
        val textMovieName = findViewById<TextView>(R.id.textMovieName)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)

        Glide.with(this).load("https://image.tmdb.org/t/p/w370_and_h556_bestv2${movie.poster_path}")
            .into(imageLogo)
        textMovieName.text = movie.title
        textViewDescription.text = movie.overview
    }
}
