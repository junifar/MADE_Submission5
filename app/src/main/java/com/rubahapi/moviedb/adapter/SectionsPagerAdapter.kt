package com.rubahapi.moviedb.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.rubahapi.moviedb.main.fragment.movie.MovieFragment
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.main.fragment.tvshow.TvShowFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_movie,
    R.string.tab_tv_show
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var positionState = 0

    override fun getItem(position: Int): Fragment {
        this.positionState = position
        return when (position) {
            0 -> MovieFragment.newInstance()
            else -> TvShowFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}