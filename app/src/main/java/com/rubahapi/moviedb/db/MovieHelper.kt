package com.rubahapi.moviedb.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rubahapi.moviedb.db.MovieCatalogueDB.Companion.TABLE_MOVIE
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.OVERVIEW
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.POSTER_PATH
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.TITLE
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion._ID
import com.rubahapi.moviedb.model.Movie
import java.lang.Exception
import java.sql.SQLException

class MovieHelper(context: Context) {
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var instance: MovieHelper
    private lateinit var database: SQLiteDatabase
    private val databaseTable = TABLE_MOVIE

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

    fun getAllMovie():ArrayList<Movie>{
        val arrayList = arrayListOf<Movie>()
        val cursor = database.query(false,
            databaseTable,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            while (!cursor.isAfterLast){
                val movie = Movie(
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH))
                )
                arrayList.add(movie)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return arrayList
    }

    fun insertMovie(movie: Movie):Long{
        val args = ContentValues()
        args.put(TITLE, movie.title)
        args.put(OVERVIEW, movie.overview)
        args.put(POSTER_PATH, movie.poster_path)
        return database.insert(databaseTable, null, args)
    }

    fun updateMovie(movie: Movie):Int{
        val args = ContentValues()
        args.put(TITLE, movie.title)
        args.put(OVERVIEW, movie.overview)
        args.put(POSTER_PATH, movie.poster_path)
        return database.update(databaseTable, args, "$_ID = ${movie.title}", null)
    }

    fun deleteMovie(id:Int):Int{
        return database.delete(databaseTable, "$_ID = $id", null)
    }
}

val Context.database: MovieHelper
get() = MovieHelper(applicationContext).getInstance(applicationContext)