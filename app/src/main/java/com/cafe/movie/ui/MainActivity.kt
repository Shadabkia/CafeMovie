package com.cafe.movie.ui

import android.content.res.Configuration
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
import com.cafe.movie.utils.CoreUtils
import com.cafe.movie.utils.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MovieListener{

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    private val movieAdapter = MoviesAdapter(this)

    private var spanCount = 3;
    private val layoutManager =  GridLayoutManager(this,spanCount)


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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 6; // Set span count for landscape orientation
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 3; // Set span count for portrait orientation
        }

        layoutManager.spanCount = spanCount
        binding.rvMovies.layoutManager = layoutManager

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

            lifecycleScope.launch {
                movieAdapter.loadStateFlow.collectLatest {
                    Timber.tag("movieadapter").d("loadStateFlow $it")
                    Timber.tag("loadStateFlow").d("append ${it.append} prepend ${it.prepend} refresh ${it.refresh}")

                    clLogoLoading.isVisible = it.refresh is LoadState.Loading

                    clLoadMoreError.isVisible = it.append is LoadState.Error
                    pbLoadMore.isVisible = it.append is LoadState.Loading

                    if(it.refresh is LoadState.Error ) {
                        clMainError.isVisible = (it.refresh as LoadState.Error).error.message == "No Data"
                    } else
                        clMainError.isVisible = false
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btLoadMoreTry.setOnClickListener{
                movieAdapter.retry()
            }

            btRetry.setOnClickListener {
                movieAdapter.refresh()
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                movieAdapter.refresh()
            }
        }
    }

    override fun onMovieClicked(view: View, movieId: Int?) {

    }
}