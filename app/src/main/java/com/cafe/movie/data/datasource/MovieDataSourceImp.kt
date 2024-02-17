package com.cafe.movie.data.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.data.network.dto.MovieListResponse
import com.cafe.movie.data.network.service.MovieService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDataSourceImp @Inject constructor(
    private val service: MovieService,
) : MovieRemoteDataSource {
    override suspend fun getMovieList(page: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MovieListPagingSource(service, 20)
            }
        ).flow
    }
}