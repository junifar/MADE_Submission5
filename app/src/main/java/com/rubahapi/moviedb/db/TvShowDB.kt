package com.rubahapi.moviedb.db

import android.provider.BaseColumns



class TvShowDB {
    companion object{
        const val TABLE_TV_SHOW = "tv_show_db"
    }

    internal class TvShowColumns : BaseColumns {
        companion object {
            val _ID = "ID"
            val TITLE = "title"
            val OVERVIEW = "overview"
            val POSTER_PATH = "poster_path"
        }
    }
}