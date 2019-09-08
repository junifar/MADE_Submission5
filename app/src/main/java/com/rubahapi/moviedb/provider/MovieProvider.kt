package com.rubahapi.moviedb.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rubahapi.moviedb.db.MovieCatalogueDB.Companion.AUTHORITY
import com.rubahapi.moviedb.db.MovieCatalogueDB.Companion.TABLE_MOVIE
import com.rubahapi.moviedb.db.MovieHelper


class MovieProvider:ContentProvider(){

    private lateinit var movieHelper: MovieHelper


    companion object {
        private val MOVIEP = 1
        private val MOVIEP_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIEP)

            sUriMatcher.addURI(AUTHORITY, "$TABLE_MOVIE/#", MOVIEP_ID)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        //TODO:a
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        movieHelper.open()
        var cursor: Cursor? = null
        when(sUriMatcher.match(uri)){
            MOVIEP ->{
                cursor = movieHelper.queryProvider()
            }
            MOVIEP_ID->{
                cursor = movieHelper.queryByIdProvider(uri.lastPathSegment?:"")
            }
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        movieHelper = MovieHelper(context!!).getInstance(context!!)
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}