package com.cafe.movie.ui.adapter

import android.view.View

interface MovieListener {
    fun onMovieClicked(view: View, movieId: Int?)
}
