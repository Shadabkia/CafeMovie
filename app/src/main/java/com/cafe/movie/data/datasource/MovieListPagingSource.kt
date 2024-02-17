package com.cafe.movie.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.data.network.service.MovieService
import retrofit2.HttpException
import java.io.IOException

class MovieListPagingSource(
    private val api: MovieService,
    private val size: Int
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.getMovieList(position/20 + 1)


            val movies = if (response.isSuccessful)
                response.body()!!.movies
            else {
                emptyList()
            }

            if(movies.isEmpty()){
                return LoadResult.Error(Exception("No Data"))
            } else {
                LoadResult.Page(
                    data = movies!!,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - size,
                    nextKey = if (movies.isEmpty()) null else position + size
                )
            }



        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int = 0

    companion object {
        const val STARTING_PAGE_INDEX = 0
    }
}