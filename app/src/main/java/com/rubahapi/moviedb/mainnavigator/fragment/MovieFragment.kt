package com.rubahapi.moviedb.mainnavigator.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.adapter.SectionsPagerAdapter


class MovieFragment: Fragment(){

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


    fun filterList(search:String){
        println(search)
    }
}