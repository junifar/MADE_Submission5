package com.rubahapi.moviedb.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.db.MovieHelper

/**
 * Implementation of App Widget functionality.
 */
class LatestMovie : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val database = MovieHelper(context)
            val movieHelper = database.getInstance(context)
            movieHelper.open()
            val result = movieHelper.getAllTvShow()

            var countMovie = 0

//            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.latest_movie)
            views.setTextViewText(R.id.appwidget_movie, result.size.toString())
            views.setTextViewText(R.id.appwidget_tvshow, countMovie.toString())

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

