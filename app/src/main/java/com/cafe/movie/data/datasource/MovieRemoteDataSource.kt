package com.cafe.movie.data.datasource

import com.cafe.movie.data.network.Result
import com.cafe.movie.data.network.dto.MovieListResponse
import kotlinx.coroutines.flow.Flow

interface MovieRemoteDataSource {
    suspend fun getMovieList(
        page: Int,
    ): Flow<Result<MovieListResponse>>
}