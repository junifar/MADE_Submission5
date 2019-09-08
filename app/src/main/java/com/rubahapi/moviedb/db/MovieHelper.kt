package com.rubahapi.moviedb.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import com.rubahapi.moviedb.db.MovieCatalogueDB.Companion.TABLE_MOVIE
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.OVERVIEW
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.POSTER_PATH
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion.TITLE
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion._ID
import com.rubahapi.moviedb.db.TvShowDB.Companion.TABLE_TV_SHOW
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.TvShow
import java.lang.Exception
import java.sql.SQLException

class MovieHelper(context: Context) {
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var instance: MovieHelper
    private lateinit var database: SQLiteDatabase
    private val databaseTable = TABLE_MOVIE
    private val databaseTableTvShow = TABLE_TV_SHOW

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

    fun query(): ArrayList<Movie> {
        val arrayList = ArrayList<Movie>()
        val cursor = database.query(databaseTable, null, null, null, null, null, "$_ID DESC", null)
        cursor.moveToFirst()
        var note: ContactsContract.CommonDataKinds.Note
        if (cursor.count > 0) {
            do {
                val movie = Movie(
                    cursor.getInt(cursor.getColumnIndexOrThrow(_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH))

                )
                arrayList.add(movie)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun getTvShowByID(id:Int):ArrayList<TvShow>{
        val arrayList = arrayListOf<TvShow>()
        val cursor = database.query(false,
            databaseTableTvShow,
            null,
            "$_ID = $id",
            null,
            null,
            null,
            "$_ID ASC",
            null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            while (!cursor.isAfterLast){
                val tvshow = TvShow(
                    cursor.getString(cursor.getColumnIndexOrThrow(_ID)).toInt(),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH))
                )
                arrayList.add(tvshow)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return arrayList
    }

    fun getAllTvShow():ArrayList<TvShow>{
        val arrayList = arrayListOf<TvShow>()
        val cursor = database.query(false,
            databaseTableTvShow,
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
                val tvshow = TvShow(
                    cursor.getString(cursor.getColumnIndexOrThrow(_ID)).toInt(),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH))
                )
                arrayList.add(tvshow)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return arrayList
    }

    fun getMovieByID(id:Int):ArrayList<Movie>{
        val arrayList = arrayListOf<Movie>()
        val cursor = database.query(false,
            databaseTable,
            null,
            "$_ID = $id",
            null,
            null,
            null,
            "$_ID ASC",
            null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            while (!cursor.isAfterLast){
                val movie = Movie(
                    cursor.getString(cursor.getColumnIndexOrThrow(_ID)).toInt(),
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
                    cursor.getString(cursor.getColumnIndexOrThrow(_ID)).toInt(),
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
        args.put(_ID, movie.id)
        args.put(TITLE, movie.title)
        args.put(OVERVIEW, movie.overview)
        args.put(POSTER_PATH, movie.poster_path)
        return database.insert(databaseTable, null, args)
    }

    fun insertTvShow(tvShow: TvShow):Long{
        val args = ContentValues()
        args.put(_ID, tvShow.id)
        args.put(TITLE, tvShow.name)
        args.put(OVERVIEW, tvShow.overview)
        args.put(POSTER_PATH, tvShow.poster_path)
        return database.insert(databaseTableTvShow, null, args)
    }

    fun deleteTvShow(id:Int):Int{
        return database.delete(databaseTableTvShow, "$_ID = $id", null)
    }

    fun deleteMovie(id:Int):Int{
        return database.delete(databaseTable, "$_ID = $id", null)
    }

    fun insertProvider(values: ContentValues): Long {
        return database.insert(databaseTable, null, values)
    }

    fun updateProvider(id: String, values: ContentValues): Int {
        return database.update(databaseTable, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteProvider(id: String): Int {
        return database.delete(databaseTable, "$_ID = ?", arrayOf(id))
    }

    fun queryByIdProvider(id: String): Cursor {
        return database.query(databaseTable, null, "$_ID = ?", arrayOf(id), null, null, null, null)
    }

    fun queryProvider(): Cursor {
        return database.query(databaseTable, null, null, null, null, null, "$_ID ASC")
    }
}

val Context.database: MovieHelper
    get() = MovieHelper(applicationContext).getInstance(applicationContext)