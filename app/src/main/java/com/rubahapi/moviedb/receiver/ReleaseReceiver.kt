package com.rubahapi.moviedb.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.api.TheMovieDbApi
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReleaseReceiver:BroadcastReceiver(){
    companion object{
        val TYPE_REPEATING_DAILY_RELEASE = "RepeatingAlarm1"
        val EXTRA_MESSAGE_RELEASE = "message1"
        val EXTRA_TYPE_RELEASE = "type1"
    }

    private val ID_REPEATING_RELEASE = 102

    private val TIME_FORMAT = "HH:mm"

    fun getReleaseToday(context: Context){
        GlobalScope.launch(Dispatchers.Main) {
            val repository = ApiRepository()
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val data = Gson().fromJson(repository.doRequest(TheMovieDbApi.getTodayRelease(formatter.format(Date()))).await(), MovieResponse::class.java)
            if (data.movies.isNotEmpty()) {
                showAlarmNotification(context, ID_REPEATING_RELEASE,"", data.movies)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE_RELEASE)
        val message = intent.getStringExtra(EXTRA_MESSAGE_RELEASE)
        val title = TYPE_REPEATING_DAILY_RELEASE
        val notifId = ID_REPEATING_RELEASE
        getReleaseToday(context)
        showToast(context, title, message)
    }

    private fun showToast(context: Context, title: String, message: String) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }

    private fun showAlarmNotification(
        context: Context,
        notifId: Int,
        message: String,
        movies: List<Movie>
    ) {
        val CHANNEL_ID = "Channel_2 "
        val CHANNEL_NAME = "AlarmManager channel"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val inboxStyle = NotificationCompat.InboxStyle()
//            .addLine("New Email from " + stackNotif[idNotification].sender)
//            .addLine("New Email from " + stackNotif[idNotification - 1].sender)
            .setBigContentTitle("${movies.size} new Movies waiting to watch")
            .setSummaryText("mail@dicoding")
        movies.forEach {
            inboxStyle.addLine(it.title)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mail_white_48px)
            .setContentTitle("Catalogue Movie")
            .setContentText("${movies.size} released today. you must watch it")
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setStyle(inboxStyle)
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }

    }

    fun setRepeatingAlarm(context: Context, type: String, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE_RELEASE, message)
        intent.putExtra(EXTRA_TYPE_RELEASE, type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING_RELEASE, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseReceiver::class.java)
        val requestCode = ID_REPEATING_RELEASE
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show()
    }

}