package com.rubahapi.moviedb.mainnavigator.fragment

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.main.fragment.movie.MovieFragment
import com.rubahapi.moviedb.main.fragment.tvshow.TvShowFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_movie,
    R.string.tab_tv_show
)

class MovieFragment: Fragment(){

    lateinit var viewPager: ViewPager
    lateinit var movieFragment: MovieFragment
    lateinit var tvShowFragment: TvShowFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewLayout = inflater.inflate(R.layout.activity_main_movie_fragment, container, false)
        val sectionsPagerAdapter = SectionsPagerAdapter(viewLayout.context, childFragmentManager)
        viewPager = viewLayout.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = viewLayout.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        movieFragment = sectionsPagerAdapter.getItem(0) as MovieFragment
        val toolbar = viewLayout.findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.main_menu)
        setHasOptionsMenu(true)
        return viewLayout
    }


    fun filterList(search:String){

        movieFragment.filterList(search)
        tvShowFragment.filterList(search)
    }

    inner class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var positionState = 0

        override fun getItem(position: Int): Fragment {
            this.positionState = position
            return when (position) {
                0 -> {
                    movieFragment = MovieFragment()
                    val args = Bundle()
                    movieFragment.arguments = args
                    movieFragment
                }
                else -> {
                    tvShowFragment = TvShowFragment()
                    val args = Bundle()
                    tvShowFragment.arguments = args
                    tvShowFragment
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context.resources.getString(TAB_TITLES[position])
        }

        override fun getCount(): Int {
            return TAB_TITLES.size
        }
    }
}