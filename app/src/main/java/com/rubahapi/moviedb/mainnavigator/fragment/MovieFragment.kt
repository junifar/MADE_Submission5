package com.rubahapi.moviedb.mainnavigator.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.adapter.SectionsPagerAdapter


class MovieFragment: Fragment(), SearchView.OnQueryTextListener{

    private lateinit var menuItem: MenuItem
    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewLayout = inflater.inflate(R.layout.activity_main_movie_fragment, container, false)
        val sectionsPagerAdapter = SectionsPagerAdapter(viewLayout.context, childFragmentManager)
        viewPager = viewLayout.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = viewLayout.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val toolbar = viewLayout.findViewById<android.support.v7.widget.Toolbar>(R.id.main_toolbar)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.main_menu)
        setHasOptionsMenu(true)
        return viewLayout
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = (activity as AppCompatActivity).getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView

        menuItem = menu.findItem(R.id.action_search) as MenuItem

        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo((activity as AppCompatActivity).componentName)
        )
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_change_settings){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}