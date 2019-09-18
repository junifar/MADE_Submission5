package com.rubahapi.moviedb.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.rubahapi.moviedb.db.DatabaseHelper
import com.rubahapi.moviedb.db.MovieCatalogueDB.MovieColumns.Companion._ID
import com.rubahapi.moviedb.db.TvShowDB.Companion.TABLE_TV_SHOW

class TVShowProvider : ContentProvider() {

    private var myDB: DatabaseHelper? = null

    private val TVSHOWS = 1
    private val TVSHOWS_ID = 2

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    companion object {
        const val AUTHORITY = "com.rubahapi.moviedb.provider.MovieProvider"
        val CONTENT_URI_TV_SHOW : Uri = Uri.parse("content://" + AUTHORITY + "/" +
                TABLE_TV_SHOW)
    }

    init {
        sURIMatcher.addURI(AUTHORITY,
            TABLE_TV_SHOW, TVSHOWS)
        sURIMatcher.addURI(AUTHORITY, "$TABLE_TV_SHOW/#",
            TVSHOWS_ID)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val uriType = sURIMatcher.match(uri)
        val sqlDB = myDB!!.writableDatabase
        val id: Long
        when (uriType) {
            TVSHOWS -> id = sqlDB.insert(TABLE_TV_SHOW, null, values)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return Uri.parse("$TABLE_TV_SHOW/$id")
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_TV_SHOW
        when (sURIMatcher.match(uri)) {
            TVSHOWS_ID -> queryBuilder.appendWhere(_ID+ "="
                    + uri.lastPathSegment)
            TVSHOWS -> {
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
            TVSHOWS -> rowsUpdated = sqlDB.update(
                TABLE_TV_SHOW,
                values,
                selection,
                selectionArgs)
            TVSHOWS_ID -> {
                val id = uri.lastPathSegment
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(
                        TABLE_TV_SHOW,
                        values,
                        "$_ID=$id", null)
                } else {
                    rowsUpdated = sqlDB.update(
                        TABLE_TV_SHOW,
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
            TVSHOWS -> rowsDeleted = sqlDB.delete(
                TABLE_TV_SHOW,
                selection,
                selectionArgs)
            TVSHOWS_ID -> {
                val id = uri.lastPathSegment
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                        TABLE_TV_SHOW,
                        "$_ID=$id",
                        null)
                } else {
                    rowsDeleted = sqlDB.delete(
                        TABLE_TV_SHOW,
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