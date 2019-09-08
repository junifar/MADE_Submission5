package com.rubahapi.moviedb.db

import android.net.Uri
import android.provider.BaseColumns


class MovieCatalogueDB {
    companion object{
        const val TABLE_MOVIE = "movie_catalogue_db"
        val AUTHORITY = "com.rubahapi.moviedb"
        private val SCHEME = "content"
    }

    internal class MovieColumns : BaseColumns {
        companion object {
            val _ID = "ID"
            val TITLE = "title"
            val OVERVIEW = "overview"
            val POSTER_PATH = "poster_path"
            val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build()
        }
    }
}