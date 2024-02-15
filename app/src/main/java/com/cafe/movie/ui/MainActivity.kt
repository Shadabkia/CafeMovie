package com.cafe.movie.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.cafe.movie.databinding.ActivityMainBinding
import com.cafe.movie.ui.adapter.MovieListener
import com.cafe.movie.ui.adapter.MoviesAdapter
import com.cafe.movie.utils.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MovieListener{

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    val movieAdapter = MoviesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainActivityEvents.collect {
                    when (it) {
                        MainActivityEvents.InitViews -> initViews()

                    }
                }
            }
        }
        viewModel.activityCreated()

        Timber.tag("moooshi").d("onCreate: ")
    }

    private fun initViews() {

        initListeners()

        lifecycleScope.launch {
            viewModel.movies.collect {
                if (it != null) {
                    movieAdapter.submitData(it)
                }
            }
        }



        binding.apply {
            rvMovies.apply {
                itemAnimator = null
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
                adapter = movieAdapter
            }

            swipeRefresh.setOnRefreshListener {
                movieAdapter.refresh()
            }

            lifecycleScope.launch {
                movieAdapter.loadStateFlow.collectLatest {
                    Timber.tag("movieadapter").d("loadStateFlow $it")
                    swipeRefresh.isRefreshing = it.refresh is LoadState.Loading

                    clTryAgain.isVisible = it.append is LoadState.Error
                    pbLoadMore.isVisible = it.append is LoadState.Loading

                }
            }

            movieAdapter.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    Timber.tag("append").d("addLoadStateListener ${loadState.append.endOfPaginationReached}")
//                    binding.srlEmptyList.isVisible = transactionAdapter.itemCount < 1
//                    binding.appbar.isVisible = transactionAdapter.itemCount > 0
//                    binding.nsvTransaction.isVisible = transactionAdapter.itemCount > 0
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btTry.setOnClickListener{
                movieAdapter.retry()
            }
        }
    }

    override fun onMovieClicked(view: View, movieId: Int?) {

    }
}