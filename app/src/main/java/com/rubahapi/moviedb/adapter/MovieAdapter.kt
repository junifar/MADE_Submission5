package com.rubahapi.moviedb.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rubahapi.moviedb.BuildConfig.IMAGE_LINK_URL
import com.rubahapi.moviedb.R
import com.rubahapi.moviedb.model.Movie

class MovieAdapter(private val items: List<Movie>, private val listener: (Movie) -> Unit):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.list_item_movie, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindItem(holder, items[position], listener)


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val name = view.findViewById<TextView>(R.id.listview_item_title)
        private val description = view.findViewById<TextView>(R.id.listview_item_short_description)
        private val imagePath = view.findViewById<ImageView>(R.id.image_logo)

        fun bindItem(holder: ViewHolder, items: Movie, listener: (Movie) -> Unit){
            name.text = items.title
            description.text = items.overview

            Glide.with(holder.imagePath.context).load("$IMAGE_LINK_URL${items.poster_path}")
                .into(imagePath)
            itemView.setOnClickListener{listener(items)}
        }
    }

}