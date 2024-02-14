package com.cafe.movie.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.ui.adapter.viewHolder.MovieViewHolder

class MoviesAdapter(
    private val listener : MovieListener
) : ListAdapter<Movie, MovieViewHolder>(DiffCallBack()) {

    private class DiffCallBack : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ) =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieViewHolder.create(parent, listener, parent.context)

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(getItem(position))


}