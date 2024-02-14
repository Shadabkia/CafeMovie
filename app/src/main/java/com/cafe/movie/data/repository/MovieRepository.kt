package com.cafe.movie.data.repository

import com.cafe.movie.data.network.Result
import com.cafe.movie.data.network.dto.MovieListResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieList(page: Int) : Flow<Result<MovieListResponse>>
}