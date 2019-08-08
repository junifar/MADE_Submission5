package com.rubahapi.moviedb.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        val DATABASE_NAME = "movie_catalogue_db"
        val DATABASE_VERSION = 1
        val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE ${MovieCatalogueDB.TABLE_MOVIE} " +
                "(${MovieCatalogueDB.MovieColumns._ID} INTEGER PRIMARY_KEY AUTONCREEMENT, " +
                "${MovieCatalogueDB.MovieColumns.TITLE} TEXT NOT NULL, " +
                "${MovieCatalogueDB.MovieColumns.OVERVIEW} TEXT NOT NULL, " +
                "${MovieCatalogueDB.MovieColumns.POSTER_PATH} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${MovieCatalogueDB.TABLE_MOVIE}")
        onCreate(db)
    }
}
