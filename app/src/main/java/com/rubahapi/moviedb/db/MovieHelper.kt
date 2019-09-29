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
import com.rubahapi.moviedb.db.TvShowDB.Companion.TABLE_TV_SHOW
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.TvShow
import com.rubahapi.moviedb.provider.MovieProvider
import com.rubahapi.moviedb.provider.TVShowProvider
import java.lang.Exception
import java.sql.SQLException

class MovieHelper(context: Context) {
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var instance: MovieHelper
    private lateinit var database: SQLiteDatabase
    private val databaseTable = MovieCatalogueDB.TABLE_MOVIE
    private val databaseTableTvShow = TABLE_TV_SHOW
    private val myCR: ContentResolver = context.contentResolver

    fun getInstance(context: Context): MovieHelper{
//        if (instance == null){
            synchronized(SQLiteOpenHelper::class){
//                if(instance == null){
                    instance = MovieHelper(context)
//                }
            }
//        }
        return  instance
    }

    fun open(){
        try {
            database = databaseHelper.writableDatabase
            throw SQLException()
        }catch (e: Exception){

        }
    }

    fun close(){
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun getTvShowByID(id:Int):ArrayList<TvShow>{
        val cursor = myCR.query(TVShowProvider.CONTENT_URI_TV_SHOW, null, "$_ID = $id", null, null)
        val listMovie = arrayListOf<TvShow>()
        cursor.moveToFirst()
        if (cursor.count > 0){
            while (!cursor.isAfterLast){
                val movie = TvShow(
                    cursor.getString(cursor.getColumnIndex(_ID)).toInt(),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(POSTER_PATH))
                )
                listMovie.add(movie)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return listMovie
    }

    fun getAllTvShow():ArrayList<TvShow>{
        val cursor = myCR.query(TVShowProvider.CONTENT_URI_TV_SHOW, null, null, null, null)
        val listTvShow = arrayListOf<TvShow>()
        cursor.moveToFirst()
        if (cursor.count > 0){
            while (!cursor.isAfterLast){
                val tvShow = TvShow(
                    cursor.getString(cursor.getColumnIndex(_ID)).toInt(),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(POSTER_PATH))
                )
                listTvShow.add(tvShow)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return listTvShow
    }

    fun getMovieByID(id:Int):ArrayList<Movie>{
        val cursor = myCR.query(MovieProvider.CONTENT_URI, null, "$_ID = $id", null, null)
        val listMovie = arrayListOf<Movie>()
        cursor.moveToFirst()
        if (cursor.count > 0){
            while (!cursor.isAfterLast){
                val movie = Movie(
                    cursor.getString(cursor.getColumnIndex(_ID)).toInt(),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(POSTER_PATH))
                )
                listMovie.add(movie)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return listMovie
    }

    fun getAllMovie():ArrayList<Movie>{
        val cursor = myCR.query(MovieProvider.CONTENT_URI, null, null, null, null)
        val listMovie = arrayListOf<Movie>()
        cursor.moveToFirst()
        if (cursor.count > 0){
            while (!cursor.isAfterLast){
                val movie = Movie(
                    cursor.getString(cursor.getColumnIndex(_ID)).toInt(),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(POSTER_PATH))
                )
                listMovie.add(movie)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return listMovie
    }

    fun insertMovie(movie: Movie):Long{
        val values = ContentValues()
        values.put(_ID, movie.id)
        values.put(TITLE, movie.title)
        values.put(OVERVIEW, movie.overview)
        values.put(POSTER_PATH, movie.poster_path)
        myCR.insert(MovieProvider.CONTENT_URI, values)
        return 0
    }

    fun insertTvShow(tvShow: TvShow):Long{
        val values = ContentValues()
        values.put(TvShowDB.TvShowColumns._ID, tvShow.id)
        values.put(TvShowDB.TvShowColumns.OVERVIEW, tvShow.overview)
        values.put(TvShowDB.TvShowColumns.TITLE, tvShow.name)
        values.put(TvShowDB.TvShowColumns.POSTER_PATH, tvShow.poster_path)
        myCR.insert(TVShowProvider.CONTENT_URI_TV_SHOW, values)
        return 0
    }

    fun updateTvShow(tvShow: TvShow):Int{
        val args = ContentValues()
        args.put(TITLE, tvShow.name)
        args.put(OVERVIEW, tvShow.overview)
        args.put(POSTER_PATH, tvShow.poster_path)
        return database.update(databaseTableTvShow, args, "$_ID = ${tvShow.name}", null)
    }

    fun deleteTvShow(id:Int):Int{
        return myCR.delete(TVShowProvider.CONTENT_URI_TV_SHOW, "$_ID = $id", null)
//        return database.delete(databaseTableTvShow, "$_ID = $id", null)
    }

    fun updateMovie(movie: Movie):Int{
        val args = ContentValues()
        args.put(TITLE, movie.title)
        args.put(OVERVIEW, movie.overview)
        args.put(POSTER_PATH, movie.poster_path)
        return database.update(databaseTable, args, "$_ID = ${movie.title}", null)
    }

    fun deleteMovie(id:Int):Int{
        return myCR.delete(MovieProvider.CONTENT_URI, "$_ID = $id", null)
//        return database.delete(databaseTable, "$_ID = $id", null)
    }
}

val Context.database: MovieHelper
get() = MovieHelper(applicationContext).getInstance(applicationContext)