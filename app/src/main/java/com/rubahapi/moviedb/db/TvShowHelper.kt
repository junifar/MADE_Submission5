package com.rubahapi.moviedb.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rubahapi.moviedb.db.TvShowDB.Companion.TABLE_TV_SHOW
import com.rubahapi.moviedb.db.TvShowDB.TvShowColumns.Companion.OVERVIEW
import com.rubahapi.moviedb.db.TvShowDB.TvShowColumns.Companion.POSTER_PATH
import com.rubahapi.moviedb.db.TvShowDB.TvShowColumns.Companion.TITLE
import com.rubahapi.moviedb.db.TvShowDB.TvShowColumns.Companion._ID
import com.rubahapi.moviedb.model.TvShow
import java.lang.Exception
import java.sql.SQLException

class TvShowHelper(context: Context) {
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var instance: TvShowHelper
    private lateinit var database: SQLiteDatabase
    private val databaseTable = TABLE_TV_SHOW

    fun getInstance(context: Context): TvShowHelper{
//        if (instance == null){
            synchronized(SQLiteOpenHelper::class){
//                if(instance == null){
                    instance = TvShowHelper(context)
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

    fun getAllMovie():ArrayList<TvShow>{
        val arrayList = arrayListOf<TvShow>()
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
                val tvShow = TvShow(
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH))
                )
                arrayList.add(tvShow)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return arrayList
    }

    fun insertTvShow(tvShow: TvShow):Long{
        val args = ContentValues()
        args.put(TITLE, tvShow.name)
        args.put(OVERVIEW, tvShow.overview)
        args.put(POSTER_PATH, tvShow.poster_path)
        return database.insert(databaseTable, null, args)
    }

    fun updateMovie(tvShow: TvShow):Int{
        val args = ContentValues()
        args.put(TITLE, tvShow.name)
        args.put(OVERVIEW, tvShow.overview)
        args.put(POSTER_PATH, tvShow.poster_path)
        return database.update(databaseTable, args, "$_ID = ${tvShow.name}", null)
    }

    fun deleteMovie(id:Int):Int{
        return database.delete(databaseTable, "$_ID = $id", null)
    }
}

val Context.Tvshow: TvShowHelper
get() = TvShowHelper(applicationContext).getInstance(applicationContext)