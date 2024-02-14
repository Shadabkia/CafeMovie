package com.cafe.movie.data.network.dto


import com.squareup.moshi.Json

data class MovieListResponse(
    @Json(name = "page")
    val page: Int?,
    @Json(name = "results")
    val movies: List<Movie?>?,
    @Json(name = "total_pages")
    val totalPages: Int?,
    @Json(name = "total_results")
    val totalResults: Int?
)