package com.indialone.appwidgetsample

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import java.text.DateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */

private const val mSharedPrefFile = "com.indialone.appwidgetsample"
private const val COUNT_KEY = "count"

class NewAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

@SuppressLint("RemoteViewLayout")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val pref: SharedPreferences = context.getSharedPreferences(mSharedPrefFile, 0)
    var count = pref.getInt(COUNT_KEY + appWidgetId, 0)
    count += 1

    val dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())

//    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)

    views.setTextViewText(R.id.appwidget_id, "$appWidgetId")

    views.setTextViewText(
        R.id.appwidget_update,
        context.resources.getString(R.string.date_count_format, count, dateString)
    )

    val prefEditor: SharedPreferences.Editor = pref.edit()
    prefEditor.putInt(COUNT_KEY + appWidgetId, count)
    prefEditor.apply()

    val intentUpdate = Intent(context, NewAppWidget::class.java)
    intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)

    val idArray: Array<Int> = arrayOf(appWidgetId)

    intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)

    val pendingUpdate = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        intentUpdate,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    views.setOnClickPendingIntent(R.id.button_update, pendingUpdate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

