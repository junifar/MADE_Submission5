package com.rubahapi.moviedb.db

import android.provider.BaseColumns



class MovieCatalogueDB {
    companion object{
        const val TABLE_MOVIE = "movie_catalogue_db"
    }

    internal class MovieColumns : BaseColumns {
        companion object {
            val _ID = "ID"
            val TITLE = "title"
            val OVERVIEW = "overview"
            val POSTER_PATH = "poster_path"
        }
    }
}