package com.cafe.movie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cafe.movie.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.cafe.movie.data.network.Result
import com.cafe.movie.data.network.dto.Movie
import com.cafe.movie.data.network.dto.MovieListResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<PagingData<Movie>?>(null)
    val movies: StateFlow<PagingData<Movie>?> get() = _movies

    private val mainActivityEventsChannel = Channel<MainActivityEvents>()
    val mainActivityEvents = mainActivityEventsChannel.receiveAsFlow()

    fun activityCreated() = viewModelScope.launch {
        mainActivityEventsChannel.send(MainActivityEvents.InitViews)
    }

    init {
        getMovieList(1)
    }

    private fun getMovieList(page: Int) = viewModelScope.launch {
        movieRepository.getMovieList(page = page)
            .cachedIn(viewModelScope)
            .collectLatest { result ->
                _movies.value = result
            }
    }
}