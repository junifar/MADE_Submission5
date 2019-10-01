package com.rubahapi.moviedb.mainnavigator

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.format.DateUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.app.NotificationCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.SettingsActivity
import com.rubahapi.moviedb.mainnavigator.fragment.MovieFragment
import com.rubahapi.moviedb.mainnavigator.fragment.favorite.FavoriteFragment
import com.rubahapi.moviedb.model.NotificationItem
import com.rubahapi.moviedb.receiver.NotifReceiver
import com.rubahapi.moviedb.receiver.ReleaseReceiver
import com.rubahapi.moviedb.receiver.ReleaseReceiver.Companion.TYPE_REPEATING_DAILY_RELEASE
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*




class MainActivityNavigator : AppCompatActivity(), SearchView.OnQueryTextListener{

    private lateinit var menuItem: MenuItem
    private val fm = supportFragmentManager
    private lateinit var alarmManager: AlarmManager

    companion object{
        private const val NOTIF_REQUEST_CODE = 200
        private const val CHANNEL_NAME = "Movie DB channel"
        private const val GROUP_KEY_EMAILS = "group_key_emails"

        private var idNotif = 0
        private const val maxNotif = 2

        private val stackNotif = ArrayList<NotificationItem>()
    }

    private fun sendNotif() {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_white_48px)
        val intent = Intent(this, MainActivityNavigator::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIF_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

//        //still wrong here
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 19)
//        calendar.set(Calendar.MINUTE, 23)
//        calendar.set(Calendar.SECOND, 0)
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        val mBuilder: NotificationCompat.Builder

        //Melakukan pengecekan jika idNotif lebih kecil dari Max Notif
        val CHANNEL_ID = "channel_01"
        if (idNotif < maxNotif) {
            mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New Email from " + stackNotif[idNotif].sender)
                .setContentText(stackNotif[idNotif].message)
                .setSmallIcon(R.drawable.ic_mail_white_48px)
                .setLargeIcon(largeIcon)
                .setGroup(GROUP_KEY_EMAILS)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine("New Email from " + stackNotif[idNotif].sender)
                .addLine("New Email from " + stackNotif[idNotif - 1].sender)
                .setBigContentTitle("$idNotif new emails")
                .setSummaryText("mail@dicoding")
            mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("$idNotif new emails")
                .setContentText("mail@dicoding.com")
                .setSmallIcon(R.drawable.ic_mail_white_48px)
                .setGroup(GROUP_KEY_EMAILS)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        }
        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        Materi ini akan dibahas lebih lanjut di modul extended
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            mBuilder.setChannelId(CHANNEL_ID)

            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()

        mNotificationManager.notify(idNotif, notification)
    }

    private val runnable = Runnable { sendNotifier() }

    private fun sendNotifier(){
        stackNotif.add(NotificationItem(idNotif, "Rubahapi", "Test"))
        sendNotif()
        idNotif++
        Handler().postDelayed(runnable, 5000)
    }

    override fun onQueryTextSubmit(search: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(search: String?): Boolean {
        val fragment = fm.findFragmentById(R.id.home_container) as MovieFragment
        if (search != null) {
            fragment.filterList(search)
        }
        return true
    }

    private  var savedInstanceState:Bundle = Bundle()
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_movie -> {
                loadMovieFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
                loadFavoriteFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadMovieFragment() {
            val fragment = MovieFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_container, fragment, MovieFragment::class.java.simpleName)
                .commit()
    }

    private fun loadFavoriteFragment() {
            val fragment = FavoriteFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_container, fragment, FavoriteFragment::class.java.simpleName)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigator)
        val navView: BottomNavigationView = findViewById(R.id.navigation_view)

        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState
        }



        val pref = PreferenceManager.getDefaultSharedPreferences(this)

//        val releaseReceiver = ReleaseReceiver()
//        releaseReceiver.setRepeatingAlarm(this, TYPE_REPEATING_DAILY_RELEASE, "01:20", "test")
//        releaseReceiver.setRepeatingAlarm(this, TYPE_REPEATING_DAILY_RELEASE, "01:20", "test")

        val dailyReminder = pref.getBoolean("daily_reminder",false)
        val receiver = NotifReceiver()
        if (dailyReminder){
            //daily reminder
            receiver.setRepeatingAlarm(this, NotifReceiver.TYPE_REPEATING, "07:00", "test")
//            alarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            sendNotifier()
        }else if (!dailyReminder){
            receiver.cancelAlarm(this, NotifReceiver.TYPE_REPEATING)
        }

        val releaseReminder = pref.getBoolean("release_reminder", false)
        val releaseReceiver = ReleaseReceiver()
        if(releaseReminder){
            releaseReceiver.setRepeatingAlarm(this, TYPE_REPEATING_DAILY_RELEASE, "08:00", "test")
        }else if (!releaseReminder){
            releaseReceiver.cancelAlarm(this)
        }

        loadMovieFragment()
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView

        menuItem = menu.findItem(R.id.action_search) as MenuItem

        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(this.componentName)
        )
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onCreateOptionsMenu(menu: Menu) {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        val searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
//
//        menuItem = menu.findItem(R.id.action_search) as MenuItem
//
//        searchView.setSearchableInfo(
//            searchManager
//                .getSearchableInfo(this.componentName)
//        )
//        searchView.maxWidth = Integer.MAX_VALUE
//
//        searchView.setOnQueryTextListener(this)
//        super.onCreateOptionsMenu(menu)
//    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_change_settings){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        if(item?.itemId == R.id.action_settings){
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
