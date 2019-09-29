package com.rubahapi.moviedb.widget

import com.rubahapi.moviedb.R
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.rubahapi.moviedb.BuildConfig
import com.rubahapi.moviedb.db.MovieHelper

import java.util.ArrayList

/**
 * Created by dicoding on 1/9/2017.
 */


internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()


    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val database = MovieHelper(mContext)
        val movieHelper = database.getInstance(mContext)
        movieHelper.open()
        val result = movieHelper.getAllTvShow()

        result.forEach {
            mWidgetItems.add(Glide.with(mContext).asBitmap().load("${BuildConfig.IMAGE_LINK_URL}${it.poster_path}")
                .submit().get())
        }

        val tvShowHelper = database.getInstance(mContext)
        tvShowHelper.open()
        val resultTvShow = tvShowHelper.getAllMovie()
        resultTvShow.forEach {
            mWidgetItems.add(Glide.with(mContext).asBitmap().load("${BuildConfig.IMAGE_LINK_URL}${it.poster_path}")
                .submit().get())
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = Bundle()
        extras.putInt(LatestMovie.EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

}