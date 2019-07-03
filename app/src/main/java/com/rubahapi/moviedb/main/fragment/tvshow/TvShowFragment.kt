package com.rubahapi.moviedb.main.fragment.tvshow


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
import com.rubahapi.moviedb.adapter.TvShowAdapter
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.main.fragment.movie.MoviePresenter
import com.rubahapi.moviedb.model.TvShow
import com.rubahapi.moviedb.util.invisible
import com.rubahapi.moviedb.util.visible

class TvShowFragment : Fragment(), TVShowView {
    private var items: MutableList<TvShow> = mutableListOf()
    lateinit var adapter: TvShowAdapter
    lateinit var progressBar: ProgressBar
    lateinit var presenter: TvShowPresenter
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var list:RecyclerView

    private fun initComponent(){
        val request = ApiRepository()
        val gson = Gson()
        progressBar = activity?.findViewById(R.id.progressBar) as ProgressBar
        swipeRefresh = activity?.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        adapter = TvShowAdapter(context!!, items){
            val intent = Intent(activity, DetailMovieActivity::class.java)
            intent.putExtra(
                DetailMovieActivity.EXTRA_DETAIL_ACTIVITY_TYPE,
                DetailMovieActivity.EXTRA_DETAIL_TV_SHOW
            )
            intent.putExtra(DetailMovieActivity.EXTRA_DETAIL_TV_SHOW, it)
            startActivity(intent)
        }
        list.adapter = adapter

        presenter = TvShowPresenter(this, request, gson)
        onAttachView()
        presenter.getTvShow()

        swipeRefresh.setOnRefreshListener {
            presenter.getTvShow()
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
        val view = inflater.inflate(R.layout.fragment_tv_show, container, false)
        list = view.findViewById(R.id.recycler_view_tv_show)

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

    override fun showTvShow(data: List<TvShow>) {
        swipeRefresh.isRefreshing = false
        items.clear()
        items.addAll(data)
        adapter.notifyDataSetChanged()
    }

    companion object{
        @JvmStatic
        fun newInstance(): TvShowFragment {
            return TvShowFragment().apply {
                arguments = Bundle().apply {}
            }
        }

    }

}
