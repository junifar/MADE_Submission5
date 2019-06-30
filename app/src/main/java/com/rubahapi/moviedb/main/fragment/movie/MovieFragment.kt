package com.rubahapi.moviedb.main.fragment.movie


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.rubahapi.moviedb.DetailMovieActivity
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.adapter.MovieAdapter
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.util.invisible
import com.rubahapi.moviedb.util.visible

class MovieFragment : Fragment(), MovieView {
    private var items: MutableList<Movie> = mutableListOf()
    lateinit var progressBar: ProgressBar
    lateinit var presenter: MoviePresenter
    lateinit var swipeRefresh: SwipeRefreshLayout

    private fun initComponent(){
        progressBar = activity?.findViewById(R.id.progressBar) as ProgressBar
        swipeRefresh = activity?.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        val list = view.findViewById<RecyclerView>(R.id.recycler_view_movie)

        generateData()

        list.layoutManager = LinearLayoutManager(context)
        list.adapter = MovieAdapter(context, items){
            val intent = Intent(activity, DetailMovieActivity::class.java)
            intent.putExtra(
                DetailMovieActivity.EXTRA_DETAIL_ACTIVITY_TYPE,
                DetailMovieActivity.EXTRA_DETAIL_MOVIE
            )
            intent.putExtra(DetailMovieActivity.EXTRA_DETAIL_MOVIE, it)
            startActivity(intent)
        }
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
    }

    private fun generateData(){
        val dataName = resources.getStringArray(R.array.movie_name)
        val dataDescription = resources.getStringArray(R.array.movie_description)
        val dataImage = resources.obtainTypedArray(R.array.movie_photo)
        items.clear()
        for (i in 0 until dataName.size-1){
            items.add(Movie(dataName[i], dataDescription[i], dataImage.getResourceId(i, -1)))
        }
        dataImage.recycle()
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showBlankData() {
        swipeRefresh.isRefreshing = false
    }

    override fun onAttachView() {
        presenter.onAttach(this)
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    companion object{
        @JvmStatic
        fun newInstance(): MovieFragment {
            return MovieFragment().apply {
                arguments = Bundle().apply {}
            }
        }
    }
}
