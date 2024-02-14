package com.cafe.movie.data.datasource

import com.cafe.movie.data.network.Result
import com.cafe.movie.data.network.SafeApiRequest
import com.cafe.movie.data.network.dto.MovieListResponse
import com.cafe.movie.data.network.service.MovieService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDataSourceImp @Inject constructor(
    private val service: MovieService,

) : MovieRemoteDataSource, SafeApiRequest() {
    override suspend fun getMovieList(page: Int): Flow<Result<MovieListResponse>> =
        apiRequest { service.getMovieList(page = page) }
}