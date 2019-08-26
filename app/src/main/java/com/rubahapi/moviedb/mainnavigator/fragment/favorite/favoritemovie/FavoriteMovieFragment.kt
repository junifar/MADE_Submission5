package com.rubahapi.moviedb.mainnavigator.fragment.favorite.favoritemovie


import android.content.Intent
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
import com.rubahapi.moviedb.db.MovieHelper
import com.rubahapi.moviedb.model.Movie
import com.rubahapi.moviedb.util.invisible
import com.rubahapi.moviedb.util.visible

class FavoriteMovieFragment : Fragment(), FavoriteMovieView {
    private var items: ArrayList<Movie> = arrayListOf()
    private lateinit var adapter: MovieAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: FavoriteMoviePresenter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var list:RecyclerView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ITEM_DATA_SAVED, items)
    }

    private fun getMovieDB():List<Movie>{
        val database = MovieHelper(this.requireContext())
        val movieHelper = database.getInstance(this.requireContext())
        movieHelper.open()
        val result = movieHelper.getAllMovie()
        movieHelper.close()
        return result
    }

    private fun initComponent(savedInstanceState: Bundle?){
        progressBar = activity?.findViewById(R.id.progressBar) as ProgressBar
        swipeRefresh = activity?.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout

        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(context!!, android.R.color.holo_green_dark))

//        val context = this.context
//        if(context != null){
//            adapter = MovieAdapter(context, items){
//                val intent = Intent(activity, DetailMovieActivity::class.java)
//                intent.putExtra(
//                    DetailMovieActivity.EXTRA_DETAIL_ACTIVITY_TYPE,
//                    DetailMovieActivity.EXTRA_DETAIL_MOVIE
//                )
//                intent.putExtra(DetailMovieActivity.EXTRA_DETAIL_MOVIE, it)
//                startActivity(intent)
//            }
//        }

        adapter = MovieAdapter(items){
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
        presenter = FavoriteMoviePresenter(this, request, gson)
        onAttachView()

        if (savedInstanceState == null){
//            presenter.getMovie()
            val result = getMovieDB()
            if (result == null){
                val data= mutableListOf<Movie>()
                data.add(Movie(0,"No Data Show","",""))
                showMovie(data)
            }else{
                showMovie(result)
            }
        }else{
            items.clear()
            savedInstanceState.getParcelableArrayList<Movie>(ITEM_DATA_SAVED).forEach {
                movie ->
                items.add(movie)
            }
            adapter.notifyDataSetChanged()
        }

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
        initComponent(savedInstanceState)
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
        const val ITEM_DATA_SAVED = "itemsData"
    }
}
