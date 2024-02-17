package com.cafe.movie.data.network.service

import com.cafe.movie.data.network.dto.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {

    @GET("discover/movie")
    suspend fun getMovieList(
        @retrofit2.http.Query("page") page: Int,
        @retrofit2.http.Query("release_date.gte") minDate: String = "{min_date}",
        @retrofit2.http.Query("release_date.lte") maxDate: String = "{max_date}",
        @retrofit2.http.Query("sort_by") sortBy: String = "popularity.desc",
        @retrofit2.http.Query("include_adult") includeAdult: Boolean = false,
        @retrofit2.http.Query("include_video") includeVideo: Boolean = false,
        @retrofit2.http.Query("language") language: String = "en-US",
        @retrofit2.http.Query("with_release_type") withReleaseType: String = "2|3"
    ): Response<MovieListResponse>

}