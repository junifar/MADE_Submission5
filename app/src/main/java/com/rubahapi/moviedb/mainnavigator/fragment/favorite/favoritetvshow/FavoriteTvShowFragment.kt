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
import com.rubahapi.moviedb.adapter.TvShowAdapter
import com.rubahapi.moviedb.api.ApiRepository
import com.rubahapi.moviedb.model.TvShow
import com.rubahapi.moviedb.util.invisible
import com.rubahapi.moviedb.util.visible

class FavoriteTvShowFragment : Fragment(), FavoriteTVShowView {
    private var items: ArrayList<TvShow> = arrayListOf()
    private lateinit var adapter: TvShowAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: FavoriteTvShowPresenter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var list:RecyclerView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ITEM_DATA_SAVED, items)
    }

    private fun initComponent(savedInstanceState: Bundle?){
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

        presenter = FavoriteTvShowPresenter(this, request, gson)
        onAttachView()

        if(savedInstanceState == null){
            presenter.getTvShow()
        }else{
            items.clear()
            savedInstanceState.getParcelableArrayList<TvShow>(ITEM_DATA_SAVED).forEach {
                tvShow->
                items.add(tvShow)
            }
            adapter.notifyDataSetChanged()
        }

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
        initComponent(savedInstanceState)
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
        const val ITEM_DATA_SAVED = "itemsData"
        @JvmStatic
        fun newInstance(): FavoriteTvShowFragment {
            return FavoriteTvShowFragment().apply {
                arguments = Bundle().apply {}
            }
        }

    }

}
