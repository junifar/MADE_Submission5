package com.rubahapi.moviedb.main.fragment.movie


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import com.rubahapi.moviedb.DetailMovieActivity
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.adapter.MovieAdapter
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.util.invisible
import com.rubahapi.moviedb.util.visible

class MovieFragment : Fragment(), MovieView {
    private var items: MutableList<Movie> = mutableListOf()
    lateinit var adapter: MovieAdapter
    lateinit var progressBar: ProgressBar
    lateinit var presenter: MoviePresenter
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var list:RecyclerView

    private fun initComponent(){
        progressBar = activity?.findViewById(R.id.progressBar) as ProgressBar
        swipeRefresh = activity?.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout

        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(context!!, android.R.color.holo_green_dark))

        adapter = MovieAdapter(context!!, items){
            val intent = Intent(activity, DetailMovieActivity::class.java)
            intent.putExtra(
                DetailMovieActivity.EXTRA_DETAIL_ACTIVITY_TYPE,
                DetailMovieActivity.EXTRA_DETAIL_MOVIE
            )
            intent.putExtra(DetailMovieActivity.EXTRA_DETAIL_MOVIE, it)
            startActivity(intent)
        }
        list.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = MoviePresenter(this, request, gson)
        onAttachView()
        presenter.getMovie()

        swipeRefresh.setOnRefreshListener {
            presenter.getMovie()
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        list = view.findViewById(R.id.recycler_view_movie)

        list.layoutManager = LinearLayoutManager(context)

        return view

    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun onAttachView() {
        presenter.onAttach(this)
    }

    override fun onDetachView() {
        presenter.onDetach()
    }

    override fun showMovie(data: List<Movie>) {
        swipeRefresh.isRefreshing = false
        items.clear()
        items.addAll(data)
        adapter.notifyDataSetChanged()
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
