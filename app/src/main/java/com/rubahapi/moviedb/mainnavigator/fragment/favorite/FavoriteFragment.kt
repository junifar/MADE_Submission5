package com.rubahapi.moviedb.mainnavigator.fragment.favorite


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*

import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.adapter.SectionsFavoritePagerAdapter
import com.rubahapi.moviedb.adapter.SectionsPagerAdapter

class FavoriteFragment : Fragment() {

    lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewLayout = inflater.inflate(R.layout.activity_main_favorite_fragment, container, false)
        val sectionsFavoritePagerAdapter = SectionsFavoritePagerAdapter(viewLayout.context, childFragmentManager)
        viewPager = viewLayout.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsFavoritePagerAdapter
        val tabs: TabLayout = viewLayout.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val toolbar = viewLayout.findViewById<android.support.v7.widget.Toolbar>(R.id.main_toolbar)
        toolbar.inflateMenu(R.menu.main_menu)

        (activity as AppCompatActivity).title = "Your Favorite Movie"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        return viewLayout
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
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
