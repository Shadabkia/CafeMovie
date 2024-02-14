package com.cafe.movie.ui.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cafe.movie.R
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.databinding.ItemMovieBinding
import com.cafe.movie.ui.adapter.MovieListener

class MovieViewHolder(
    private val binding: ItemMovieBinding,
    private val listener: MovieListener,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            listener: MovieListener,
            context: Context
        ): MovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMovieBinding.inflate(inflater, parent, false)
            return MovieViewHolder(binding, listener, context)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(movie: Movie) {
        binding.apply {


            tvMovieTitle.text = movie.title

            rbRate.rating = movie.voteAverage?.toFloat()!!

            Glide
                .with(context)
                .load("https://image.tmdb.org/t/p/w300"+movie.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .placeholder(R.drawable.ic_avatar)
                .into(ivCover)

            root.setOnClickListener {
                listener.onMovieClicked(binding.root, movie.id)
            }
        }
    }
}
