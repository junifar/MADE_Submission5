package com.rubahapi.moviedb.mainnavigator.fragment.favorite


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritemovie.FavoriteMovieFragment
import com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritetvshow.FavoriteTvShowFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_movie,
    R.string.tab_tv_show
)


class FavoriteFragment : Fragment() {

    lateinit var viewPager: ViewPager
    lateinit var favoriteMovieFragment: FavoriteMovieFragment
    lateinit var favoriteTvShowFragment: FavoriteTvShowFragment

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

        val toolbar = viewLayout.findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
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

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item?.itemId == R.id.action_change_settings){
//            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//            startActivity(intent)
//        }
//        return super.onOptionsItemSelected(item)
//    }

    inner class SectionsFavoritePagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var positionState = 0

        override fun getItem(position: Int): Fragment {
            this.positionState = position
            return when (position) {
                0 -> {
                    favoriteMovieFragment = FavoriteMovieFragment()
                    val args = Bundle()
                    favoriteMovieFragment.arguments = args
                    favoriteMovieFragment
                }else
                -> {
                    favoriteTvShowFragment = FavoriteTvShowFragment()
                    val args = Bundle()
                    favoriteTvShowFragment.arguments = args
                    favoriteTvShowFragment
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
