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
        adapter = MovieAdapter(items){}
        list.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = MoviePresenter(this, request, gson)
        presenter.getMovie()

        swipeRefresh.setOnRefreshListener {
            presenter.getMovie()
        }
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

        generateData()

        list.layoutManager = LinearLayoutManager(context)
//        list.adapter = MovieAdapter(context, items){
//            val intent = Intent(activity, DetailMovieActivity::class.java)
//            intent.putExtra(
//                DetailMovieActivity.EXTRA_DETAIL_ACTIVITY_TYPE,
//                DetailMovieActivity.EXTRA_DETAIL_MOVIE
//            )
//            intent.putExtra(DetailMovieActivity.EXTRA_DETAIL_MOVIE, it)
//            startActivity(intent)
//        }
        return view

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
