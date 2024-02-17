package com.cafe.movie.data.repository

import androidx.paging.PagingData
import com.cafe.movie.data.network.Result
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.data.network.dto.MovieListResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieList(page: Int) : Flow<PagingData<Movie>>
}