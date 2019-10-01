package com.rubahapi.moviedb.main.fragment.movie


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class MovieFragment : Fragment(), MovieView{

    private var items: ArrayList<Movie> = arrayListOf()
    private lateinit var listItems:List<Movie>
    private lateinit var adapter: MovieAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: MoviePresenter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var list: RecyclerView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ITEM_DATA_SAVED, items)
    }

    private fun initComponent(savedInstanceState: Bundle?){
        progressBar = activity?.findViewById(R.id.progressBar) as ProgressBar
        swipeRefresh = activity?.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout

        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(context!!, android.R.color.holo_green_dark))

//        val context = this.context
//        if(context != null){
//            adapter = MovieAdapter(items){
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
        presenter = MoviePresenter(this, request, gson)
        onAttachView()

        if (savedInstanceState == null){
            presenter.getMovie()
            adapter.notifyDataSetChanged()
        }else{
            items.clear()
            savedInstanceState.getParcelableArrayList<Movie>(ITEM_DATA_SAVED)?.forEach {
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
        listItems = data
        adapter.notifyDataSetChanged()
    }

    fun filterList(textFilter:String){
        val dataFilter = listItems.filter { it.title?.contains(textFilter, true)?:false}
        items.clear()
        items.addAll(dataFilter)
        adapter.notifyDataSetChanged()
    }

    override fun showBlankMovie(){
        swipeRefresh.isRefreshing = false
        items.clear()
        this.listItems = mutableListOf()
        adapter.notifyDataSetChanged()
    }

    companion object{
        const val ITEM_DATA_SAVED = "itemsData"
    }
}
