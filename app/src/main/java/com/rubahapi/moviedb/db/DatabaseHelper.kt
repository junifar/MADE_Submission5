package com.rubahapi.moviedb.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "movie_catalogue_db"
        const val DATABASE_VERSION = 2
        val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE ${MovieCatalogueDB.TABLE_MOVIE} " +
                "(${MovieCatalogueDB.MovieColumns._ID} INTEGER PRIMARY_KEY AUTOINCREEMENT, " +
                "${MovieCatalogueDB.MovieColumns.TITLE} TEXT NOT NULL, " +
                "${MovieCatalogueDB.MovieColumns.OVERVIEW} TEXT NOT NULL, " +
                "${MovieCatalogueDB.MovieColumns.POSTER_PATH} TEXT NOT NULL)"

        val SQL_CREATE_TABLE_TV_SHOW = "CREATE TABLE ${TvShowDB.TABLE_TV_SHOW} " +
                "(${TvShowDB.TvShowColumns._ID} INTEGER PRIMARY_KEY AUTOINCREEMENT, " +
                "${TvShowDB.TvShowColumns.TITLE} TEXT NOT NULL, " +
                "${TvShowDB.TvShowColumns.OVERVIEW} TEXT NOT NULL, " +
                "${TvShowDB.TvShowColumns.POSTER_PATH} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
        db?.execSQL(SQL_CREATE_TABLE_TV_SHOW)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${MovieCatalogueDB.TABLE_MOVIE}")
        db?.execSQL("DROP TABLE IF EXISTS ${TvShowDB.TABLE_TV_SHOW}")
        onCreate(db)
    }
}
