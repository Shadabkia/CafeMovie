package com.cafe.movie.data.repository

import com.cafe.movie.data.datasource.MovieRemoteDataSource
import com.cafe.movie.data.network.Result
import com.cafe.movie.data.network.dto.MovieListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(
    private val dataSource: MovieRemoteDataSource
) : MovieRepository{
    override suspend fun getMovieList(page: Int): Flow<Result<MovieListResponse>> =
        dataSource.getMovieList(page = page)

}