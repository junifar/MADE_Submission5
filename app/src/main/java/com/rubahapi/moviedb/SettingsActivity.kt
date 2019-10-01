package com.rubahapi.moviedb

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.rubahapi.moviedb.receiver.NotifReceiver
import com.rubahapi.moviedb.receiver.ReleaseReceiver

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences,
            key: String?
        ) {
            val dailyReminder = sharedPreferences.getBoolean("daily_reminder",false)
            val releaseReminder = sharedPreferences.getBoolean("release_reminder", false)
            val receiver = NotifReceiver()
            if (dailyReminder){
                receiver.setRepeatingAlarm(requireContext(), NotifReceiver.TYPE_REPEATING, "07:00", "test")
            }else if (!dailyReminder){
                receiver.cancelAlarm(requireContext(), NotifReceiver.TYPE_REPEATING)
            }

            val releaseReceiver = ReleaseReceiver()
            if(releaseReminder){
                releaseReceiver.setRepeatingAlarm(requireContext(),ReleaseReceiver.TYPE_REPEATING_DAILY_RELEASE, "08:00", "test")
            }else if (!releaseReminder){
                releaseReceiver.cancelAlarm(requireContext())
            }
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}