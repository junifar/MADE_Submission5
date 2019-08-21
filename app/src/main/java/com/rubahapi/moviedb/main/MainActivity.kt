package com.rubahapi.moviedb.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.adapter.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.main_menu)
    }
}