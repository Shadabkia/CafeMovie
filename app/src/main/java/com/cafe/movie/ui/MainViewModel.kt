package com.cafe.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafe.movie.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.cafe.movie.data.network.Result
import com.cafe.movie.data.network.dto.MovieListResponse

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val movies = MutableStateFlow<MovieListResponse?>(null)

    init {
        getMovieList(1)
    }

    private fun getMovieList(page: Int) = viewModelScope.launch {
        movieRepository.getMovieList(page = page).collectLatest { result ->

            when (result) {
                is Result.Success -> {
                    movies.value = result.data
                }

                is Result.Error -> {
                    movies.value = null
                }

                is Result.Loading -> {
                    movies.value = null
                }

            }
        }
    }
}