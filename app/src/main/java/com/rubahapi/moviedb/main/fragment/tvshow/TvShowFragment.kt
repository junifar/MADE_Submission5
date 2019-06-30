package com.rubahapi.moviedb.main.fragment.tvshow


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rubahapi.moviedb.DetailMovieActivity
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.adapter.TvShowAdapter
import com.rubahapi.moviedb.model.TvShow

class TvShowFragment : Fragment() {

    private var items: MutableList<TvShow> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tv_show, container, false)
        val list = view.findViewById<RecyclerView>(R.id.recycler_view_tv_show)

        generateData()

        list.layoutManager = LinearLayoutManager(context)
        list.adapter = TvShowAdapter(context, items){
            val intent = Intent(activity, DetailMovieActivity::class.java)
            intent.putExtra(
                DetailMovieActivity.EXTRA_DETAIL_ACTIVITY_TYPE,
                DetailMovieActivity.EXTRA_DETAIL_TV_SHOW
            )
            intent.putExtra(DetailMovieActivity.EXTRA_DETAIL_TV_SHOW, it)
            startActivity(intent)
        }

        return view
    }

    private fun generateData(){
        val dataName = resources.getStringArray(R.array.tv_show_name)
        val dataDescription = resources.getStringArray(R.array.tv_show_description)
        val dataImage = resources.obtainTypedArray(R.array.tv_show_photo)
        items.clear()
        for (i in 0 until dataName.size-1){
            items.add(TvShow(dataName[i], dataDescription[i], dataImage.getResourceId(i, -1)))
        }
        dataImage.recycle()
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
