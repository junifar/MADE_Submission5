package com.rubahapi.moviedb.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.rubahapi.moviedb.db.DatabaseHelper
import com.rubahapi.moviedb.db.MovieCatalogueDB.Companion.TABLE_MOVIE
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion._ID

class MovieProvider : ContentProvider() {

    private var myDB: DatabaseHelper? = null

    private val MOVIES = 1
    private val MOVIES_ID = 2

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    companion object {
        const val AUTHORITY = "com.rubahapi.moviedb.provider.MovieProvider"
        val CONTENT_URI : Uri = Uri.parse("content://" + AUTHORITY + "/" +
                TABLE_MOVIE)
    }

    init {
        sURIMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIES)
        sURIMatcher.addURI(AUTHORITY, "$TABLE_MOVIE/#",
            MOVIES_ID)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val uriType = sURIMatcher.match(uri)
        val sqlDB = myDB!!.writableDatabase
        val id: Long
        when (uriType) {
            MOVIES -> id = sqlDB.insert(TABLE_MOVIE, null, values)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return Uri.parse("$TABLE_MOVIE/$id")
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_MOVIE
        when (sURIMatcher.match(uri)) {
            MOVIES_ID -> queryBuilder.appendWhere(_ID+ "="
                    + uri.lastPathSegment)
            MOVIES -> {
            }
            else -> throw IllegalArgumentException("Unknown URI")
        }

        val cursor = queryBuilder.query(myDB?.readableDatabase,
            projection, selection, selectionArgs, null, null,
            sortOrder)
        cursor.setNotificationUri(context?.contentResolver,
            uri)

        return cursor
    }

    override fun onCreate(): Boolean {
        myDB = DatabaseHelper(context, null, null, 1)
        return false
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val uriType = sURIMatcher.match(uri)
        val sqlDB: SQLiteDatabase = myDB!!.writableDatabase
        val rowsUpdated: Int
        when (uriType) {
            MOVIES -> rowsUpdated = sqlDB.update(
                TABLE_MOVIE,
                values,
                selection,
                selectionArgs)
            MOVIES_ID -> {
                val id = uri.lastPathSegment
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(
                        TABLE_MOVIE,
                        values,
                        "$_ID=$id", null)
                } else {
                    rowsUpdated = sqlDB.update(
                        TABLE_MOVIE,
                        values,
                        _ID + "=" + id
                                + " and "
                                + selection,
                        selectionArgs)
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return rowsUpdated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val uriType = sURIMatcher.match(uri)
        val sqlDB = myDB!!.writableDatabase
        val rowsDeleted: Int
        when (uriType) {
            MOVIES -> rowsDeleted = sqlDB.delete(
                TABLE_MOVIE,
                selection,
                selectionArgs)
            MOVIES_ID -> {
                val id = uri.lastPathSegment
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                        TABLE_MOVIE,
                        "$_ID=$id",
                        null)
                } else {
                    rowsDeleted = sqlDB.delete(
                        TABLE_MOVIE,
                        _ID + "=" + id
                                + " and " + selection,
                        selectionArgs)
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return rowsDeleted
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Not yet implemented")
    }

}