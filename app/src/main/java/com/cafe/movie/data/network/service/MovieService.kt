package com.cafe.movie.data.network.service

import com.cafe.movie.data.network.models.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {

    // type a retrofit fun with GET method by this api
    //https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_release_type=2|3&release_date.gte={min_date}&release_date.lte={max_date}
    @GET
    suspend fun getMovieList(
        @retrofit2.http.Query("page") page: Int,
        @retrofit2.http.Query("release_date.gte") minDate: String,
        @retrofit2.http.Query("release_date.lte") maxDate: String,
        @retrofit2.http.Query("sort_by") sortBy: String,
        @retrofit2.http.Query("include_adult") includeAdult: Boolean,
        @retrofit2.http.Query("include_video") includeVideo: Boolean,
        @retrofit2.http.Query("language") language: String,
        @retrofit2.http.Query("with_release_type") withReleaseType: String
    ): Response<MovieListResponse>

}