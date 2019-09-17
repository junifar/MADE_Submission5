package com.rubahapi.moviedb.db

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.OVERVIEW
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.POSTER_PATH
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.TITLE
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion._ID
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.providers.MovieProvider.Companion.CONTENT_URI

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    var context:Context
    var name: String? = null
    private var factory:SQLiteDatabase.CursorFactory? = null
    var version:Int = 0

    private val myCR: ContentResolver = context.contentResolver

    init {
        this.context = context
    }

    constructor(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int):this(context){
        this.context = context
        this.name = name
        this.factory = factory
        this.version = version
    }

    companion object{
        const val DATABASE_NAME = "movie_catalogue_db"
        const val DATABASE_VERSION = 2
        val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE ${MovieCatalogueDB.TABLE_MOVIE} " +
                "($_ID INTEGER PRIMARY_KEY AUTOINCREEMENT, " +
                "$TITLE TEXT NOT NULL, " +
                "$OVERVIEW TEXT NOT NULL, " +
                "$POSTER_PATH TEXT NOT NULL)"

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

    fun addMovie(movie:Movie){
        val values = ContentValues()
        values.put(TITLE, movie.title)
        values.put(OVERVIEW, movie.overview)
        values.put(POSTER_PATH, movie.poster_path)
        myCR.insert(CONTENT_URI, values)
    }
}
